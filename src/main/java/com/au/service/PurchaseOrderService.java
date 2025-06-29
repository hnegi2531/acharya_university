package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.NoSuchFileException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.au.dto.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import com.au.repository.ApproverCreationRepository;
import com.au.event.LibraryEvent;
import com.au.exception.ResourceNotFoundException;
import com.au.model.EnvItemsInStores;
import com.au.model.GRN;
import com.au.model.GRN.GRN_TYPE;
import com.au.model.GrnNumberConfiguration;
import com.au.model.ItemsCreation;
import com.au.model.LibraryInventory;
import com.au.model.POReferenceNoConfig;
import com.au.model.PurchaseItems;
import com.au.model.PurchaseOrder;
import com.au.model.StockIssue;
import com.au.model.StockNumberConfiguration;
import com.au.model.StockRegisterDTO;
import com.au.model.TemporaryPurchaseItems;
import com.au.model.TemporaryPurchaseOrder;
import com.au.model.Vendor;
import com.au.repository.CityRepository;
import com.au.repository.CountryRepository;
import com.au.repository.DraftJournalVoucherRepository;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.EnvItemsInStoresRepository;
import com.au.repository.GrnNumberConfigurationRepository;
import com.au.repository.GrnRepository;
import com.au.repository.GroupRepository;
import com.au.repository.ItemsCreationRepository;
import com.au.repository.JournalVoucherRepository;
import com.au.repository.LedgerRepository;
import com.au.repository.LibraryInventoryRepository;
import com.au.repository.MeasureRepository;
import com.au.repository.PaymentVoucherRepository;
import com.au.repository.PoReferenceNoRepository;
import com.au.repository.PurchaseItemsRepository;
import com.au.repository.PurchaseOrderRepository;
import com.au.repository.StateRepository;
import com.au.repository.StockIssueRepository;
import com.au.repository.StockNumberConfigurationRepository;
import com.au.repository.StoreIndentRequestRepository;
import com.au.repository.StoresStockRepository;
import com.au.repository.TemporaryPurchaseItemsRepository;
import com.au.repository.TemporaryPurchaseOrderRepository;
import com.au.repository.VendorRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class PurchaseOrderService {

	private Logger logger = LoggerFactory.getLogger(PurchaseOrderService.class);

	@Autowired
	private TemporaryPurchaseOrderRepository temporaryPurchaseOrderRepository;

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private PoReferenceNoRepository poReferenceNoRepository;
	
	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private PurchaseItemsRepository purchaseItemsRepository;

	@Autowired
	private GrnRepository grnRepository;

	@Autowired
	private VendorRepository vendorRepository;

	@Autowired
	private StockIssueRepository stockIssueRepository;

	@Autowired
	private EnvItemsInStoresRepository envItemsInStoresRepository;

	@Autowired
	private StoresStockRepository storesStockRepository;

	@Autowired
	private GrnNumberConfigurationRepository grnNumberConfigurationRepository;

	@Autowired
	private StockNumberConfigurationRepository stockNumberConfigurationRepository;

	@Autowired
	private MeasureRepository measureRepository;

	@Autowired
	private ItemsCreationRepository itemsCreationRepository;

	@Autowired
	private LibraryInventoryRepository libraryInventoryRepository;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private ApproverCreationRepository approverCreationRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private EnvItemsInStoresService envItemsInStoresService;

	@Autowired
	private LedgerRepository ledgerRepository;

	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private StoreIndentRequestRepository storeIndentRequestRepository;
	
	@Autowired
	private TemporaryPurchaseItemsRepository temporaryPurchaseItemsRepository;
	
	@Autowired
	private DraftJournalVoucherRepository draftJournalVoucherRepository;
	
	@Autowired
	private JournalVoucherRepository journalVoucherRepository;
	
	@Autowired
	private PaymentVoucherRepository paymentVoucherRepository;
	
	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;
	
	
	@Autowired
	private ResponseHandler response_handler;

	private final static String pathSlash = "/";

	public static final String value = "GRNBucket";

	private AmazonS3 s3client;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	@Value("${amazonProperties.bucketName}")
	private String bucketName;
	@Value("${amazonProperties.accessKey}")
	private String accessKey;
	@Value("${amazonProperties.secretKey}")
	private String secretKey;

	@SuppressWarnings("deprecation")
	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}

	private final ModelMapper modelMapper = new ModelMapper();

	public ResponseEntity<Object> draftPurchaseOrder(TemporaryPurchaseOrderDTO temporaryPurchaseOrderDTO,
			String jwtToken) {
		JwtDetails jwtDetails = modelMapper.map((ResponseHandler.getUserDetailsByToken(jwtToken)).getBody(),
				JwtDetails.class);
		TemporaryPurchaseOrder temporaryPurchaseOrder = modelMapper.map(temporaryPurchaseOrderDTO,
				TemporaryPurchaseOrder.class);
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
		temporaryPurchaseOrder.setQuotationDate(new Date());
		temporaryPurchaseOrder.setYear(currentYear);
		temporaryPurchaseOrder.setMonth(currentMonth);
		String vendor = vendorRepository.getvendorIdByVendorName(temporaryPurchaseOrderDTO.getVendorId());
		Vendor vendorDetail = vendorRepository.getVendorById(temporaryPurchaseOrderDTO.getVendorId());
		temporaryPurchaseOrder.setVoucherHeadNewId(
				ObjectUtils.isNotEmpty(vendorDetail) ? vendorDetail.getVoucher_head_new_id() : null);
		temporaryPurchaseOrder.setVendorId(temporaryPurchaseOrderDTO.getVendorId());
		temporaryPurchaseOrder.setVendor(vendor);
		temporaryPurchaseOrder.setInstituteId(temporaryPurchaseOrderDTO.getInstituteId());
		temporaryPurchaseOrder.setEmpId(temporaryPurchaseOrderDTO.getEmpId());
		temporaryPurchaseOrder.setInstitute(temporaryPurchaseOrderDTO.getInstitute());
		temporaryPurchaseOrder.setAnnexureText(temporaryPurchaseOrderDTO.getAnnexureText());
		temporaryPurchaseOrder.setCreatedBy(jwtDetails.getUserId());
		temporaryPurchaseOrder.setCreatedUsername(jwtDetails.getUserName());
//		setTemporaryPurchaseOrder(temporaryPurchaseOrder.getTemporaryPurchaseItems(), temporaryPurchaseOrder);
		List<TemporaryPurchaseItems> temporaryPurchaseItemsList = setTemporaryPurchaseOrderAndEnvItemsInStores(
				temporaryPurchaseOrderDTO.getTemporaryPurchaseItems(), temporaryPurchaseOrder, jwtDetails);
		temporaryPurchaseOrder.setTemporaryPurchaseItems(temporaryPurchaseItemsList);
		temporaryPurchaseOrderRepository.save(temporaryPurchaseOrder);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS",
				temporaryPurchaseOrder.getTemporary_purchase_order_id());
	}

	private List<TemporaryPurchaseItems> setTemporaryPurchaseOrderAndEnvItemsInStores(
			List<TemporaryPurchaseItemsDTO> temporaryPurchaseItemsDTO, TemporaryPurchaseOrder temporaryPurchaseOrder,
			JwtDetails jwtDetails) {
		List<TemporaryPurchaseItems> temporaryPurchaseItemsList = new ArrayList<TemporaryPurchaseItems>();
		if (ObjectUtils.isNotEmpty(temporaryPurchaseItemsDTO)) {
			temporaryPurchaseItemsDTO.stream().forEach(tpidto -> {
				TemporaryPurchaseItems tpi = modelMapper.map(tpidto, TemporaryPurchaseItems.class);
				tpi.setTemporaryPurchaseOrder(temporaryPurchaseOrder);
				tpi.setBalanceQuantity(tpidto.getQuantity());
//				List<EnvItemsInStores> envItemsInStores=Arrays.asList(envItemsInStoresService.get(tpidto.getEnvItemsInStoresId()));
				tpi.setEnvItemsInStoresId(envItemsInStoresService.get(tpidto.getEnvItemsInStoresId()));
				tpi.setCreatedBy(jwtDetails.getUserId());
				tpi.setCreatedUsername(jwtDetails.getUserName());
				tpi.setActive(true);
				temporaryPurchaseItemsList.add(tpi);
			});

		}
		return temporaryPurchaseItemsList;
	}

//	private void setTemporaryPurchaseOrder(List<TemporaryPurchaseItems> temporaryPurchaseItems,
//			TemporaryPurchaseOrder temporaryPurchaseOrder) {
//		if (ObjectUtils.isNotEmpty(temporaryPurchaseItems)) {
//			temporaryPurchaseItems.stream().forEach(tpi -> {
//				tpi.setTemporaryPurchaseOrder(temporaryPurchaseOrder);
//				tpi.setBalanceQuantity(tpi.getQuantity());
//			});
//		}
//	}

	public ResponseEntity<Object> getDraftPurchaseOrder(DraftOrderRequestDTO draftOrderRequestDTO) {
		Pageable pageable = PageRequest.of(draftOrderRequestDTO.getPageNo(), draftOrderRequestDTO.getPageSize());
		Page<Object> draftOrders = temporaryPurchaseOrderRepository.getDraftPurchaseOrder(draftOrderRequestDTO,
				pageable);
		PaginationDTO paginationDTO = new PaginationDTO();
		paginationDTO.setContent(draftOrders.getContent());
		paginationDTO.setIslast(draftOrders.isLast());
		paginationDTO.setPageNo(draftOrderRequestDTO.getPageNo());
		paginationDTO.setPageSize(draftOrderRequestDTO.getPageSize());
		paginationDTO.setTotalElement(draftOrders.getTotalElements());
		paginationDTO.setTotalPage(draftOrders.getTotalPages());
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", paginationDTO);
	}

	public ResponseEntity<Object> getCancelledDraftPurchaseOrder(DraftOrderRequestDTO draftOrderRequestDTO) {
		Pageable pageable = PageRequest.of(draftOrderRequestDTO.getPageNo(), draftOrderRequestDTO.getPageSize());
		Page<Object> draftOrders = temporaryPurchaseOrderRepository.getCancelledDraftPurchaseOrder(draftOrderRequestDTO,
				pageable);
		PaginationDTO paginationDTO = new PaginationDTO();
		paginationDTO.setContent(draftOrders.getContent());
		paginationDTO.setIslast(draftOrders.isLast());
		paginationDTO.setPageNo(draftOrderRequestDTO.getPageNo());
		paginationDTO.setPageSize(draftOrderRequestDTO.getPageSize());
		paginationDTO.setTotalElement(draftOrders.getTotalElements());
		paginationDTO.setTotalPage(draftOrders.getTotalPages());
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", paginationDTO);
	}

	public ResponseEntity<Object> getDraftPurchaseOrderById(Integer id) {
		TemporaryPurchaseOrder temporaryPurchaseOrder = temporaryPurchaseOrderRepository.getDraftPurchaseOrderById(id);
		TemporaryPurchasseOrderResponseDTO temporaryPurchasseOrderResponseDTO = new TemporaryPurchasseOrderResponseDTO();
		if (ObjectUtils.isNotEmpty(temporaryPurchaseOrder)) {
			temporaryPurchasseOrderResponseDTO
					.setAccountPaymentType(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getAccountPaymentType())
							? temporaryPurchaseOrder.getAccountPaymentType()
							: null);
			temporaryPurchasseOrderResponseDTO.setActive(
					ObjectUtils.isNotEmpty(temporaryPurchaseOrder.isActive()) ? temporaryPurchaseOrder.isActive()
							: null);
			temporaryPurchasseOrderResponseDTO.setAnnexure(
					ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getAnnexure()) ? temporaryPurchaseOrder.getAnnexure()
							: null);
			
			temporaryPurchasseOrderResponseDTO.setAnnexureText(
					ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getAnnexureText()) ? temporaryPurchaseOrder.getAnnexureText()
							: null);
			
			temporaryPurchasseOrderResponseDTO
					.setApprovedDate(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getApprovedDate())
							? temporaryPurchaseOrder.getApprovedDate()
							: null);
			temporaryPurchasseOrderResponseDTO
					.setApprovedStatus(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getApprovedStatus())
							? temporaryPurchaseOrder.getApprovedStatus()
							: null);
			temporaryPurchasseOrderResponseDTO
					.setApproverId(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getApproverId())
							? temporaryPurchaseOrder.getApproverId()
							: null);
			temporaryPurchasseOrderResponseDTO
					.setCancelById(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getCancelById())
							? temporaryPurchaseOrder.getCancelById()
							: null);
			temporaryPurchasseOrderResponseDTO
					.setCreated_date(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getCreated_date())
							? temporaryPurchaseOrder.getCreated_date()
							: null);
			temporaryPurchasseOrderResponseDTO.setEmpId(
					ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getEmpId()) ? temporaryPurchaseOrder.getEmpId()
							: null);
			temporaryPurchasseOrderResponseDTO
					.setInstitute(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getInstitute())
							? temporaryPurchaseOrder.getInstitute()
							: null);
			temporaryPurchasseOrderResponseDTO
					.setInstituteId(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getInstituteId())
							? temporaryPurchaseOrder.getInstituteId()
							: null);
			temporaryPurchasseOrderResponseDTO
					.setModified_date(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getModified_date())
							? temporaryPurchaseOrder.getModified_date()
							: null);
			temporaryPurchasseOrderResponseDTO.setMonth(
					ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getMonth()) ? temporaryPurchaseOrder.getMonth()
							: null);
			temporaryPurchasseOrderResponseDTO.setNoOfDays(
					ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getNoOfDays()) ? temporaryPurchaseOrder.getNoOfDays()
							: null);
			temporaryPurchasseOrderResponseDTO
					.setPurchaseApprover(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getPurchaseApprover())
							? temporaryPurchaseOrder.getPurchaseApprover()
							: null);
			temporaryPurchasseOrderResponseDTO
					.setPurchaseApproverId(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getPurchaseApproverId())
							? temporaryPurchaseOrder.getPurchaseApproverId()
							: null);
			temporaryPurchasseOrderResponseDTO
					.setQuotationDate(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getQuotationDate())
							? temporaryPurchaseOrder.getQuotationDate()
							: null);
			temporaryPurchasseOrderResponseDTO
					.setQuotationNo(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getQuotationNo())
							? temporaryPurchaseOrder.getQuotationNo()
							: null);
			temporaryPurchasseOrderResponseDTO.setRemarks(
					ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getRemarks()) ? temporaryPurchaseOrder.getRemarks()
							: null);
			temporaryPurchasseOrderResponseDTO
					.setRequestType(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getRequestType())
							? temporaryPurchaseOrder.getRequestType()
							: null);
			temporaryPurchasseOrderResponseDTO
					.setStoreName(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getStoreName())
							? temporaryPurchaseOrder.getStoreName()
							: null);
			temporaryPurchasseOrderResponseDTO.setTemporary_purchase_order_id(
					ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getTemporary_purchase_order_id())
							? temporaryPurchaseOrder.getTemporary_purchase_order_id()
							: null);
			temporaryPurchasseOrderResponseDTO
					.setTermsAndConditions(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getTermsAndConditions())
							? temporaryPurchaseOrder.getTermsAndConditions()
							: null);
			temporaryPurchasseOrderResponseDTO
					.setTermsOfPayment(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getTermsOfPayment())
							? temporaryPurchaseOrder.getTermsOfPayment()
							: null);
			temporaryPurchasseOrderResponseDTO.setVendor(
					ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getVendor()) ? temporaryPurchaseOrder.getVendor()
							: null);
			temporaryPurchasseOrderResponseDTO.setVendorId(
					ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getVendorId()) ? temporaryPurchaseOrder.getVendorId()
							: null);
			temporaryPurchasseOrderResponseDTO.setYear(
					ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getYear()) ? temporaryPurchaseOrder.getYear() : null);
			temporaryPurchasseOrderResponseDTO
					.setDestination(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getDestination())
							? temporaryPurchaseOrder.getDestination()
							: null);
			temporaryPurchasseOrderResponseDTO
					.setOtherReference(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getOtherReference())
							? temporaryPurchaseOrder.getOtherReference()
							: null);
			Vendor vendor = vendorRepository.getVendorById(temporaryPurchaseOrder.getVendorId());
			if (ObjectUtils.isNotEmpty(vendor)) {
				temporaryPurchasseOrderResponseDTO.setBankBranch(
						ObjectUtils.isNotEmpty(vendor.getBank_branch()) ? vendor.getBank_branch() : null);
				temporaryPurchasseOrderResponseDTO.setBankName(
						ObjectUtils.isNotEmpty(vendor.getVendor_bank_name()) ? vendor.getVendor_bank_name() : null);
				temporaryPurchasseOrderResponseDTO
						.setAccountNo(ObjectUtils.isNotEmpty(vendor.getAccount_no()) ? vendor.getAccount_no() : null);
				temporaryPurchasseOrderResponseDTO
						.setAccountHolderName(ObjectUtils.isNotEmpty(vendor.getVendor_bank_account_holder_name())
								? vendor.getVendor_bank_account_holder_name()
								: null);
				temporaryPurchasseOrderResponseDTO.setBankIfscNo(
						ObjectUtils.isNotEmpty(vendor.getVendor_bank_ifsc_code()) ? vendor.getVendor_bank_ifsc_code()
								: null);
				temporaryPurchasseOrderResponseDTO.setVendorAddress(
						ObjectUtils.isNotEmpty(vendor.getVendor_address()) ? vendor.getVendor_address() : null);
				temporaryPurchasseOrderResponseDTO
						.setPinCode(ObjectUtils.isNotEmpty(vendor.getPin_code()) ? vendor.getPin_code() : null);
				String cityName = cityRepository.getCityName(
						ObjectUtils.isNotEmpty(vendor.getVendor_city_id()) ? vendor.getVendor_city_id() : 0);
				temporaryPurchasseOrderResponseDTO.setCityName(cityName);
				String stateName = stateRepository
						.getStateName(ObjectUtils.isNotEmpty(vendor.getState_id()) ? vendor.getState_id() : 0);
				temporaryPurchasseOrderResponseDTO.setStateName(stateName);
				String countryName = countryRepository
						.getCountryName(ObjectUtils.isNotEmpty(vendor.getCountry_id()) ? vendor.getCountry_id() : 0);
				temporaryPurchasseOrderResponseDTO.setCountryName(countryName);
				temporaryPurchasseOrderResponseDTO.setVendorTinNo(null);
				temporaryPurchasseOrderResponseDTO.setVendorContactNo(
						ObjectUtils.isNotEmpty(vendor.getVendor_contact_no()) ? vendor.getVendor_contact_no() : null);

				temporaryPurchasseOrderResponseDTO
						.setCreatedUsername(ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getCreatedUsername())
								? temporaryPurchaseOrder.getCreatedUsername()
								: null);
				temporaryPurchasseOrderResponseDTO.setVendorStreetName(
						ObjectUtils.isNotEmpty(vendor.getStreet_name()) ? vendor.getStreet_name() : null);
				temporaryPurchasseOrderResponseDTO
						.setPanNumber(ObjectUtils.isNotEmpty(vendor.getPan_number()) ? vendor.getPan_number() : null);
				temporaryPurchasseOrderResponseDTO.setVendorGstNo(
						ObjectUtils.isNotEmpty(vendor.getVendor_gst_no()) ? vendor.getVendor_gst_no() : null);
				temporaryPurchasseOrderResponseDTO.setVendorEmail(
						ObjectUtils.isNotEmpty(vendor.getVendor_email()) ? vendor.getVendor_email() : null);
			}
			List<TemporaryPurchaseItemResponseDTO> temporaryPurchaseItemResponseDTOs = getTemporaryPurchaseItemResponse(
					temporaryPurchaseOrder);

			temporaryPurchasseOrderResponseDTO.setTemporaryPurchaseItems(temporaryPurchaseItemResponseDTOs);

		}
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", temporaryPurchasseOrderResponseDTO);
	}

	private List<TemporaryPurchaseItemResponseDTO> getTemporaryPurchaseItemResponse(
			TemporaryPurchaseOrder temporaryPurchaseOrder) {
		List<TemporaryPurchaseItemResponseDTO> temporaryPurchaseItemResponseDTOs = new ArrayList<>();
		temporaryPurchaseOrder.getTemporaryPurchaseItems().stream().forEach(t -> {
			TemporaryPurchaseItemResponseDTO temporaryPurchaseItemResponseDTO = new TemporaryPurchaseItemResponseDTO();
			temporaryPurchaseItemResponseDTO
					.setBalanceQuantity(ObjectUtils.isNotEmpty(t.getBalanceQuantity()) ? t.getBalanceQuantity() : null);
			temporaryPurchaseItemResponseDTO
					.setCreated_date(ObjectUtils.isNotEmpty(t.getCreated_date()) ? t.getCreated_date() : null);
			temporaryPurchaseItemResponseDTO
					.setDiscount(ObjectUtils.isNotEmpty(t.getDiscount()) ? t.getDiscount() : null);
			temporaryPurchaseItemResponseDTO.setGst(ObjectUtils.isNotEmpty(t.getGst()) ? t.getGst() : null);
			temporaryPurchaseItemResponseDTO
					.setItemName(ObjectUtils.isNotEmpty(t.getItemName()) ? t.getItemName() : null);
			temporaryPurchaseItemResponseDTO
					.setModified_date(ObjectUtils.isNotEmpty(t.getModified_date()) ? t.getModified_date() : null);
			temporaryPurchaseItemResponseDTO
					.setQuantity(ObjectUtils.isNotEmpty(t.getQuantity()) ? t.getQuantity() : null);
			temporaryPurchaseItemResponseDTO.setRate(ObjectUtils.isNotEmpty(t.getRate()) ? t.getRate() : null);
			temporaryPurchaseItemResponseDTO.setTemporary_purchase_item_id(
					ObjectUtils.isNotEmpty(t.getTemporary_purchase_item_id()) ? t.getTemporary_purchase_item_id()
							: null);
			temporaryPurchaseItemResponseDTO
					.setTotalAmount(ObjectUtils.isNotEmpty(t.getTotalAmount()) ? t.getTotalAmount() : null);
			temporaryPurchaseItemResponseDTO
					.setGstTotal(ObjectUtils.isNotEmpty(t.getGstTotal()) ? t.getGstTotal() : null);
			temporaryPurchaseItemResponseDTO
					.setDiscountTotal(ObjectUtils.isNotEmpty(t.getDiscountTotal()) ? t.getDiscountTotal() : null);
			temporaryPurchaseItemResponseDTO
					.setCostTotal(ObjectUtils.isNotEmpty(t.getCostTotal()) ? t.getCostTotal() : null);

			temporaryPurchaseItemResponseDTO.setEnvItemsInStoresId(
					ObjectUtils.isNotEmpty(t.getEnvItemsInStoresId()) ? t.getEnvItemsInStoresId().getEnv_item_id()
							: null);

			temporaryPurchaseItemResponseDTO
					.setCreatedUsername(ObjectUtils.isNotEmpty(t.getCreatedUsername()) ? t.getCreatedUsername() : null);

			String measureName = envItemsInStoresRepository.getUomForGRN(ObjectUtils.isNotEmpty(
					t.getEnvItemsInStoresId()) ? t.getEnvItemsInStoresId().getEnv_item_id() : null); // changed to measure short name

			temporaryPurchaseItemResponseDTO.setMeasureName(measureName);

			temporaryPurchaseItemResponseDTOs.add(temporaryPurchaseItemResponseDTO);

		});

		return temporaryPurchaseItemResponseDTOs;
	}

	public ResponseEntity<Object> assignPurchaseApprover(String purchaseApprover, Integer id,
			Integer temporaryPurchaseOrderId) {
		TemporaryPurchaseOrder temporaryPurchaseOrder = temporaryPurchaseOrderRepository
				.getDraftPurchaseOrderById(temporaryPurchaseOrderId);
		temporaryPurchaseOrder.setPurchaseApprover(purchaseApprover);
		temporaryPurchaseOrder.setPurchaseApproverId(id);
		temporaryPurchaseOrderRepository.save(temporaryPurchaseOrder);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
	}

	public ResponseEntity<Object> cancelDraft(Integer id, Integer cancelById) {
		TemporaryPurchaseOrder temporaryPurchaseOrder = temporaryPurchaseOrderRepository.getDraftPurchaseOrderById(id);
		temporaryPurchaseOrder.setActive(false);
		temporaryPurchaseOrder.setCancelById(cancelById);

		temporaryPurchaseOrderRepository.deactivateTemporaryPurchaseItems(id);

		temporaryPurchaseOrderRepository.save(temporaryPurchaseOrder);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "CANCELLED SUCCESSFULLY", null);
	}
	
//	public ResponseEntity<Object> updateDraft(Integer temporaryPurchaseOrderId,
//			TemporaryPurchaseOrderDTOForUpdate temporaryPurchaseOrderDTO, String jwtToken) {
//		TemporaryPurchaseOrder temporaryPurchaseOrder = temporaryPurchaseOrderRepository
//				.getDraftPurchaseOrderById(temporaryPurchaseOrderId);
//		if (temporaryPurchaseOrder == null) {
//			return ResponseHandler.generateResponse(true, HttpStatus.NOT_FOUND, "Record not exists", null);
//		}
//		JwtDetails jwtDetails = modelMapper.map((ResponseHandler.getUserDetailsByToken(jwtToken)).getBody(),
//				JwtDetails.class);
//		modelMapper.map(temporaryPurchaseOrderDTO, temporaryPurchaseOrder);
//		String vendor = vendorRepository.getvendorIdByVendorName(temporaryPurchaseOrderDTO.getVendorId());
//		temporaryPurchaseOrder.setVendorId(temporaryPurchaseOrderDTO.getVendorId());
//		temporaryPurchaseOrder.setVendor(vendor);
//		temporaryPurchaseOrder.setInstituteId(temporaryPurchaseOrderDTO.getInstituteId());
//		temporaryPurchaseOrder.setInstitute(temporaryPurchaseOrderDTO.getInstitute());
//		temporaryPurchaseOrder.setAnnexureText(temporaryPurchaseOrderDTO.getAnnexureText());
//		temporaryPurchaseOrder.setModifiedBy(jwtDetails.getUserId());
//		temporaryPurchaseOrder.setModifiedUsername(jwtDetails.getUserName());
//		List<TemporaryPurchaseItems> temporaryPurchaseItems = setTemporaryPurchaseOrderAndEnvItemsInStoresForUpdate(
//				temporaryPurchaseOrderDTO.getTemporaryPurchaseItems(), temporaryPurchaseOrder, jwtDetails);
//		temporaryPurchaseOrder.setTemporaryPurchaseItems(temporaryPurchaseItems);
////		temporaryPurchaseOrder.getTemporaryPurchaseItems()
////				.forEach(item -> item.setTemporaryPurchaseOrder(temporaryPurchaseOrder));
//		temporaryPurchaseOrderRepository.save(temporaryPurchaseOrder);
//		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
//	}
//
//	private List<TemporaryPurchaseItems> setTemporaryPurchaseOrderAndEnvItemsInStoresForUpdate(
//			List<TemporaryPurchaseItemsDTOForUpdate> temporaryPurchaseItemsDTO,
//			TemporaryPurchaseOrder temporaryPurchaseOrder, JwtDetails jwtDetails) {
//		List<TemporaryPurchaseItems> temporaryPurchaseItemsList = new ArrayList<TemporaryPurchaseItems>();
//		if (ObjectUtils.isNotEmpty(temporaryPurchaseItemsDTO)) {
//			temporaryPurchaseItemsDTO.stream().forEach(tpidto -> {
//				TemporaryPurchaseItems tpi = modelMapper.map(tpidto, TemporaryPurchaseItems.class);
//				tpi.setTemporaryPurchaseOrder(temporaryPurchaseOrder);
//				tpi.setBalanceQuantity(tpidto.getQuantity());
////				List<EnvItemsInStores> envItemsInStores=Arrays.asList(envItemsInStoresService.get(tpidto.getEnvItemsInStoresId()));
//				tpi.setEnvItemsInStoresId(envItemsInStoresService.get(tpidto.getEnvItemsInStoresId()));
//				tpi.setModifiedBy(jwtDetails.getUserId());
//				tpi.setModifiedUsername(jwtDetails.getUserName());
//				temporaryPurchaseItemsList.add(tpi);
//			});
//
//		}
//		return temporaryPurchaseItemsList;
//	}

	public ResponseEntity<Object> updateDraft(Integer temporaryPurchaseOrderId, TemporaryPurchaseOrderDTOForUpdate temporaryPurchaseOrderDTO,
											  String jwtToken) {
		TemporaryPurchaseOrder temporaryPurchaseOrder = temporaryPurchaseOrderRepository.getDraftPurchaseOrderById(temporaryPurchaseOrderId);
		if (temporaryPurchaseOrder == null) {
			return ResponseHandler.generateResponse(true, HttpStatus.NOT_FOUND, "Record not exists", null);
		}
		JwtDetails jwtDetails = modelMapper.map((ResponseHandler.getUserDetailsByToken(jwtToken)).getBody(), JwtDetails.class);
		modelMapper.typeMap(TemporaryPurchaseOrderDTOForUpdate.class, TemporaryPurchaseOrder.class)
				.addMappings(mapper -> mapper.skip(TemporaryPurchaseOrder::setTemporaryPurchaseItems));
		modelMapper.map(temporaryPurchaseOrderDTO, temporaryPurchaseOrder);
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAA ");
		String vendor = "";
		try {
			vendor = vendorRepository.getvendorIdByVendorName(temporaryPurchaseOrderDTO.getVendorId());
		}catch (Exception e){
			e.printStackTrace();
		}

		temporaryPurchaseOrder.setVendor(vendor);
		temporaryPurchaseOrder.setModifiedBy(jwtDetails.getUserId());
		temporaryPurchaseOrder.setModifiedUsername(jwtDetails.getUserName());

		temporaryPurchaseItemsRepository.deleteByTemporaryPurchaseOrderId(temporaryPurchaseOrderId); // Create this repo method

		temporaryPurchaseOrder.getTemporaryPurchaseItems().clear();

		modelMapper.typeMap(TemporaryPurchaseItemsDTOForUpdate.class, TemporaryPurchaseItems.class)
				.addMappings(mapper -> mapper.skip(TemporaryPurchaseItems::setEnvItemsInStoresId));

		if (temporaryPurchaseOrderDTO.getTemporaryPurchaseItems() != null) {
			for (TemporaryPurchaseItemsDTOForUpdate itemDto : temporaryPurchaseOrderDTO.getTemporaryPurchaseItems()) {
				itemDto.setTemporary_purchase_item_id(null);
				TemporaryPurchaseItems item = modelMapper.map(itemDto, TemporaryPurchaseItems.class);
				item.setTemporaryPurchaseOrder(temporaryPurchaseOrder);
				if (itemDto.getEnvItemsInStoresId() != null) {
					EnvItemsInStores envItem = envItemsInStoresRepository.findById(itemDto.getEnvItemsInStoresId())
							.orElseThrow(() -> new RuntimeException("EnvItem with ID " + itemDto.getEnvItemsInStoresId() + " not found"));

					item.setEnvItemsInStoresId(envItem); // now it's managed
				}

				temporaryPurchaseOrder.getTemporaryPurchaseItems().add(item);
			}
		}
		TemporaryPurchaseOrder savedOrder = null;
		try {
			 savedOrder= temporaryPurchaseOrderRepository.save(temporaryPurchaseOrder);
		}catch (Exception e){
			e.printStackTrace();
		}

		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", savedOrder);
	}
	
	private List<TemporaryPurchaseItems> setTemporaryPurchaseOrderAndEnvItemsInStoresForUpdate(List<TemporaryPurchaseItemsDTOForUpdate> temporaryPurchaseItemsDTO,
																							   TemporaryPurchaseOrder temporaryPurchaseOrder, JwtDetails jwtDetails) {

		List<TemporaryPurchaseItems> temporaryPurchaseItemsList = new ArrayList<>();

		if (ObjectUtils.isNotEmpty(temporaryPurchaseItemsDTO)) {
			for (TemporaryPurchaseItemsDTOForUpdate tpidto : temporaryPurchaseItemsDTO) {

				TemporaryPurchaseItems tpi;

				if (tpidto.getTemporary_purchase_item_id() != null) {
					Optional<TemporaryPurchaseItems> existingItemOpt = temporaryPurchaseItemsRepository.findById(tpidto.getTemporary_purchase_item_id());
					tpi = existingItemOpt.get();
				} else {
					tpi = new TemporaryPurchaseItems();
				}

				// Now update the fields
				modelMapper.map(tpidto, tpi);  // This safely updates fields without replacing object
				tpi.setTemporaryPurchaseOrder(temporaryPurchaseOrder);
				tpi.setBalanceQuantity(tpidto.getQuantity());
				tpi.setEnvItemsInStoresId(envItemsInStoresService.get(tpidto.getEnvItemsInStoresId()));
				tpi.setModifiedBy(jwtDetails.getUserId());
				tpi.setModifiedUsername(jwtDetails.getUserName());

				temporaryPurchaseItemsList.add(tpi);
			}

		}
		return temporaryPurchaseItemsList;
	}

//	private List<TemporaryPurchaseItems> setTemporaryPurchaseOrderAndEnvItemsInStoresForUpdate(
//			List<TemporaryPurchaseItemsDTOForUpdate> temporaryPurchaseItemsDTO,
//			TemporaryPurchaseOrder temporaryPurchaseOrder, JwtDetails jwtDetails) {
//		List<TemporaryPurchaseItems> temporaryPurchaseItemsList = new ArrayList<TemporaryPurchaseItems>();
//		if (ObjectUtils.isNotEmpty(temporaryPurchaseItemsDTO)) {
//			temporaryPurchaseItemsDTO.stream().forEach(tpidto -> { 
//				  TemporaryPurchaseItems tpi; // Entity object to work with
//		            
//		            if (tpidto.getTemporary_purchase_item_id() != null) {
//		                // Existing item, fetch it from the database
//		                tpi = temporaryPurchaseItemsRepository.findById(tpidto.getTemporary_purchase_item_id())
//		                        .orElseThrow(() -> new IllegalArgumentException("Temporary Purchase Item not found for ID: "
//		                                + tpidto.getTemporary_purchase_item_id()));
//		                // The entity already exists, now update its values
//		            } else {
//		                // New item, create a new TemporaryPurchaseItems entity object
//		                tpi = new TemporaryPurchaseItems();
//		            }
//				System.out.println("CCCCCCCCCCCCCCCCCCCCCCCC ");
//				TemporaryPurchaseItems tpi1 = modelMapper.map(tpidto, TemporaryPurchaseItems.class);
//				tpi1.setTemporaryPurchaseOrder(temporaryPurchaseOrder);
//				tpi1.setBalanceQuantity(tpidto.getQuantity());
////				List<EnvItemsInStores> envItemsInStores=Arrays.asList(envItemsInStoresService.get(tpidto.getEnvItemsInStoresId()));
//				tpi1.setEnvItemsInStoresId(envItemsInStoresService.get(tpidto.getEnvItemsInStoresId()));
//				tpi1.setModifiedBy(jwtDetails.getUserId());
//				tpi1.setModifiedUsername(jwtDetails.getUserName());
//				temporaryPurchaseItemsList.add(tpi1);
//			});
//
//		}
//		return temporaryPurchaseItemsList;
//	}
	
//	public ResponseEntity<Object> updateDraft(Integer temporaryPurchaseOrderId,
//			TemporaryPurchaseOrderDTOForUpdate temporaryPurchaseOrderDTO, String jwtToken) {
//		TemporaryPurchaseOrder temporaryPurchaseOrder = temporaryPurchaseOrderRepository
//				.getDraftPurchaseOrderById(temporaryPurchaseOrderId);
//		if (temporaryPurchaseOrder == null) {
//			return ResponseHandler.generateResponse(true, HttpStatus.NOT_FOUND, "Record not exists", null);
//		}
//
//		JwtDetails jwtDetails = modelMapper.map((ResponseHandler.getUserDetailsByToken(jwtToken)).getBody(),
//				JwtDetails.class);
//
//		modelMapper.map(temporaryPurchaseOrderDTO, temporaryPurchaseOrder);
//		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAA ");
//
//		String vendor = vendorRepository.getvendorIdByVendorName(temporaryPurchaseOrderDTO.getVendorId());
//		System.out.println("11111111111111111111111111111 " + vendor);
//
//		temporaryPurchaseOrder.setVendorId(temporaryPurchaseOrderDTO.getVendorId());
//		temporaryPurchaseOrder.setVendor(vendor);
//		temporaryPurchaseOrder.setInstituteId(temporaryPurchaseOrderDTO.getInstituteId());
//		temporaryPurchaseOrder.setInstitute(temporaryPurchaseOrderDTO.getInstitute());
//		temporaryPurchaseOrder.setAnnexureText(temporaryPurchaseOrderDTO.getAnnexureText());
//		temporaryPurchaseOrder.setModifiedBy(jwtDetails.getUserId());
//		temporaryPurchaseOrder.setModifiedUsername(jwtDetails.getUserName());
//
//		List<TemporaryPurchaseItems> temporaryPurchaseItems = setTemporaryPurchaseOrderAndEnvItemsInStoresForUpdate(
//				temporaryPurchaseOrderDTO.getTemporaryPurchaseItems(), temporaryPurchaseOrder, jwtDetails);
//		System.out.println("BBBBBBBBBBBBBBBBBBBB ");
//
//		temporaryPurchaseOrder.setTemporaryPurchaseItems(temporaryPurchaseItems);
//		temporaryPurchaseOrderRepository.save(temporaryPurchaseOrder);
//
//		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
//	}
//
//	private List<TemporaryPurchaseItems> setTemporaryPurchaseOrderAndEnvItemsInStoresForUpdate(
//	        List<TemporaryPurchaseItemsDTOForUpdate> temporaryPurchaseItemsDTO,
//	        TemporaryPurchaseOrder temporaryPurchaseOrder, JwtDetails jwtDetails) {
//	    List<TemporaryPurchaseItems> temporaryPurchaseItemsList = new ArrayList<>();
//	    
//	    if (ObjectUtils.isNotEmpty(temporaryPurchaseItemsDTO)) {
//	        temporaryPurchaseItemsDTO.forEach(tpidto -> {
//	            System.out.println("CCCCCCCCCCCCCCCCCCCCCCCC ");
//	            
//	            TemporaryPurchaseItems tpi = null; // Entity object to work with
//	            
//	            if (tpidto.getTemporary_purchase_item_id() != null) {
//	                // Existing item, fetch it from the database
//	                tpi = temporaryPurchaseItemsRepository.findById(tpidto.getTemporary_purchase_item_id())
//	                        .orElseThrow(() -> new IllegalArgumentException("Temporary Purchase Item not found for ID: "
//	                                + tpidto.getTemporary_purchase_item_id()));
//	                // The entity already exists, now update its values
//	            } else {
//	                // New item, create a new TemporaryPurchaseItems entity object
//	                tpi = new TemporaryPurchaseItems();
//	            }
//
//	            // Map the data from the DTO to the entity
//	            modelMapper.map(tpidto, tpi);
//
//	            // Set other properties that are specific to the current session
//	            tpi.setTemporaryPurchaseOrder(temporaryPurchaseOrder);
//	            tpi.setBalanceQuantity(tpidto.getQuantity());  // Quantity
//	            tpi.setEnvItemsInStoresId(envItemsInStoresService.get(tpidto.getEnvItemsInStoresId())); // Env Items
//	            tpi.setModifiedBy(jwtDetails.getUserId());
//	            tpi.setModifiedUsername(jwtDetails.getUserName());
//
//	            // Add the entity (new or updated) to the list
//	            // Instead of directly trying to persist or update, use save (this will handle both cases)
//	            temporaryPurchaseItemsList.add(tpi);
//	        });
//	    }
//	    
//	    // Use the saveAll method to persist the list
//	    // This ensures both new and updated entities are handled correctly by the repository
//	    return temporaryPurchaseItemsRepository.saveAll(temporaryPurchaseItemsList);
//	}




	public ResponseEntity<Object> approvedDraft(Integer temporaryPurchaseOrderId, Integer approverId, String jwtToken) {
		try {
			JwtDetails jwtDetails = modelMapper.map((ResponseHandler.getUserDetailsByToken(jwtToken)).getBody(),
					JwtDetails.class);
			TemporaryPurchaseOrder temporaryPurchaseOrder = temporaryPurchaseOrderRepository
					.getDraftPurchaseOrderById(temporaryPurchaseOrderId);
			temporaryPurchaseOrder.setApproverId(approverId);
			temporaryPurchaseOrder.setPurchaseApproverId(approverId);
			temporaryPurchaseOrder.setApprovedDate(new Date());
			temporaryPurchaseOrder.setApprovedStatus(1);
			temporaryPurchaseOrderRepository.save(temporaryPurchaseOrder);
			double totalAmount = temporaryPurchaseOrder.getTemporaryPurchaseItems().stream()
					.mapToDouble(TemporaryPurchaseItems::getTotalAmount).sum();
			boolean isPurchaseOrderExists = purchaseOrderRepository
					.existsByTemporaryPurchaseOrderId(temporaryPurchaseOrderId);
			if (!isPurchaseOrderExists) {
				PurchaseOrder purchaseOrder = modelMapper.map(temporaryPurchaseOrder, PurchaseOrder.class);
				purchaseOrder.setPurchase_order_id(null);
				purchaseOrder.setPoTotalAmount(totalAmount);
				purchaseOrder.setPoReferenceNo(getReferenceNo(temporaryPurchaseOrder));
				purchaseOrder.setApproverId(approverId);
				purchaseOrder.setPurchaseApproverId(approverId);
				purchaseOrder.setTemporaryPurchaseOrderId(temporaryPurchaseOrderId);
				purchaseOrder.setModifiedBy(null);
				purchaseOrder.setModifiedUsername(null);
				Vendor vendorDetail = vendorRepository.getVendorById(temporaryPurchaseOrder.getVendorId());
				temporaryPurchaseOrder.setVoucherHeadNewId(
						ObjectUtils.isNotEmpty(vendorDetail) ? vendorDetail.getVoucher_head_new_id() : null);
				List<TemporaryPurchaseItems> temporaryPurchaseItems = temporaryPurchaseOrder
						.getTemporaryPurchaseItems();
				List<PurchaseItems> purchaseItems = new ArrayList<>();

				temporaryPurchaseItems.forEach(t -> {
					PurchaseItems purchaseItem = new PurchaseItems();
					purchaseItem.setBalanceQuantity(t.getBalanceQuantity());
					purchaseItem.setDiscount(t.getDiscount());
					purchaseItem.setGst(t.getGst());
					purchaseItem.setItemName(t.getItemName());
					purchaseItem.setPoReferenceNo(purchaseOrder.getPoReferenceNo());
					purchaseItem.setQuantity(t.getQuantity());
					purchaseItem.setRate(t.getRate());
					purchaseItem.setTotalAmount(t.getTotalAmount());
					purchaseItem.setPurchaseOrder(purchaseOrder);
					purchaseItem.setEnvItemsInStoresId(t.getEnvItemsInStoresId());
					purchaseItem.setGstTotal(t.getGstTotal());
					purchaseItem.setDiscountTotal(t.getDiscountTotal());
					purchaseItem.setCostTotal(t.getCostTotal());
					purchaseItem.setCreatedBy(jwtDetails.getUserId());
					purchaseItem.setCreatedUsername(jwtDetails.getUserName());
					purchaseItem.setModifiedBy(null);
					purchaseItem.setModifiedUsername(null);
					purchaseItems.add(purchaseItem);

				});

				purchaseOrder.setPurchaseItems(purchaseItems);
				purchaseOrderRepository.save(purchaseOrder);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, "APPROVED", null);
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.OK, "Already APPROVED", null);

			}

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", e.getMessage());

		}
	}

	private String getReferenceNo(TemporaryPurchaseOrder temporaryPurchaseOrder) {
		String referenceNo = "";
		if (ObjectUtils.isNotEmpty(temporaryPurchaseOrder)) {
			String institute = StringUtils.isNotEmpty(temporaryPurchaseOrder.getInstitute())
					? temporaryPurchaseOrder.getInstitute()
					: "";
			Integer year = ObjectUtils.isNotEmpty(temporaryPurchaseOrder.getYear()) ? temporaryPurchaseOrder.getYear()
					: null;
			POReferenceNoConfig poReferenceNoConfig = poReferenceNoRepository.findByCurrentYear(year);
			Integer count = 1;
			String formattedNumber = "";

			if (ObjectUtils.isEmpty(poReferenceNoConfig)) {
				POReferenceNoConfig configuration = new POReferenceNoConfig();
				formattedNumber = String.format("%04d", count);
				configuration.setCurrentYear(year);
				configuration.setCounter(count);
				poReferenceNoRepository.save(configuration);
			} else {
				count = poReferenceNoConfig.getCounter();
				count++;
				formattedNumber = String.format("%04d", count);
				poReferenceNoConfig.setCounter(count);
				poReferenceNoRepository.save(poReferenceNoConfig);
			}
			int lastTwoDigits = year % 100;
			referenceNo = institute + "/" + lastTwoDigits + "/" + formattedNumber;
		}
		return referenceNo;
	}

	public ResponseEntity<Object> getPurchaseOrder(DraftOrderRequestDTO draftOrderRequestDTO) {
		if (ObjectUtils.isEmpty(draftOrderRequestDTO.getFromDate()) || ObjectUtils.isEmpty(draftOrderRequestDTO.getToDate())) {
			LocalDate fromDate = LocalDate.now();
			int previousDays = 30;
			if(ObjectUtils.isNotEmpty(draftOrderRequestDTO.getPoFilter())){
				String poFilter = draftOrderRequestDTO.getPoFilter();
				if("WEEK".equalsIgnoreCase(poFilter))
					previousDays = 7;
				else if("TODAY".equalsIgnoreCase(poFilter))
					previousDays = 0;
			}
			LocalDate sevenDaysAgo = fromDate.minusDays(previousDays);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			draftOrderRequestDTO.setFromDate(formatter.format(sevenDaysAgo));
			draftOrderRequestDTO.setToDate(formatter.format(fromDate));
		}
		Pageable pageable = PageRequest.of(draftOrderRequestDTO.getPageNo(), draftOrderRequestDTO.getPageSize());
		Page<Object> purchaseOrdersPage = purchaseOrderRepository.getPurchaseOrder(draftOrderRequestDTO, pageable);

		List<PurchaseOrderResponseDTO> modifiedContent = purchaseOrdersPage.getContent().stream().map(item -> {
			PurchaseOrderResponseDTO purchaseOrder = (PurchaseOrderResponseDTO) item;
			Integer purchaseOrderId = purchaseOrder.getPurchaseOrderId();
			Integer balanceQty = purchaseItemsRepository.getBalanceByPurchaseOrderId(purchaseOrderId);

			String status = "PENDING";
			if (ObjectUtils.isNotEmpty(balanceQty)) {
				status = balanceQty > 0 ? "PENDING" : "COMPLETED";
			}
			purchaseOrder.setStatus(status);
			return purchaseOrder;
		}).collect(Collectors.toList());
		Page<PurchaseOrderResponseDTO> finalPage = new PageImpl<>(modifiedContent, pageable, purchaseOrdersPage.getTotalElements());
		PaginationDTO paginationDTO = new PaginationDTO();
		paginationDTO.setContent(finalPage.getContent());
		paginationDTO.setIslast(finalPage.isLast());
		paginationDTO.setPageNo(draftOrderRequestDTO.getPageNo());
		paginationDTO.setPageSize(draftOrderRequestDTO.getPageSize());
		paginationDTO.setTotalElement(finalPage.getTotalElements());
		paginationDTO.setTotalPage(finalPage.getTotalPages());
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", paginationDTO);
	}

	public ResponseEntity<Object> getPurchaseOrderById(Integer id) {
		PurchaseOrder purchaseOrder = purchaseOrderRepository.getPurchaseOrderById(id);
		Map<String, Object> purchaseOrderMap = new LinkedHashMap<>();
		Field[] purchaseOrderFields = purchaseOrder.getClass().getDeclaredFields();
		for (Field purchaseOrderField : purchaseOrderFields){
			purchaseOrderField.setAccessible(true);
			try {
				Object value = purchaseOrderField.get(purchaseOrder);
				purchaseOrderMap.put(purchaseOrderField.getName(), value);
			}catch (IllegalAccessException e){
				e.printStackTrace();
			}

        }

		List<Map<String, Object>> purchaseItemMapList = new ArrayList<>();
		List<PurchaseItems> purchaseItemsList = purchaseOrder.getPurchaseItems();

		for (PurchaseItems purchaseItem : purchaseItemsList){
			Map<String, Object> purchaseItemsMap = new LinkedHashMap<>();
			Field[] fields = purchaseItem.getClass().getDeclaredFields();

			for (Field field : fields){
				field.setAccessible(true);
				try {
					Object value = field.get(purchaseItem);
					if(!field.getName().equalsIgnoreCase("purchaseOrder"))
						purchaseItemsMap.put(field.getName(), value);

					if(field.getName().equalsIgnoreCase("envItemsInStoresId")){
						EnvItemsInStores envItemsInStores = (EnvItemsInStores) field.get(purchaseItem);
						Field[] envFields = envItemsInStores.getClass().getDeclaredFields();
						for (Field envField : envFields){
							envField.setAccessible(true);
							if("measure_id".equalsIgnoreCase(envField.getName())){
								Integer measureId = (Integer) envField.get(envItemsInStores);
								String measureShortName = measureRepository.findByMeasureShortNameMeasureId(measureId);
								purchaseItemsMap.put("measureShortName", measureShortName);
							}
						}
					}

				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			purchaseItemMapList.add(purchaseItemsMap);
		}
		purchaseOrderMap.put("purchaseItems", purchaseItemMapList);

		Map<String, Object> vendor = vendorRepository.getFullDetailOfVendorById(purchaseOrder.getVendorId());
		TemporaryPurchaseOrder temporaryPurchseOrder = temporaryPurchaseOrderRepository.findById(purchaseOrder.getTemporaryPurchaseOrderId())
				.orElse(new TemporaryPurchaseOrder());

		Map<String, Object> purchaseOrderWithVendorDetail = new HashMap<>();
		purchaseOrderWithVendorDetail.put("purchaseOrder", purchaseOrderMap);
		purchaseOrderWithVendorDetail.put("vendor", vendor);
		purchaseOrderWithVendorDetail.put("temporaryPurchseOrder", temporaryPurchseOrder);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", purchaseOrderWithVendorDetail);

	}

	public ResponseEntity<Object> rejectPurchaseOrder(Integer purchaseOrderId, Integer cancelById, String cancelComments) {
		PurchaseOrder purchaseOrder = purchaseOrderRepository.getPurchaseOrderById(purchaseOrderId);
		purchaseOrder.setActive(false);
		purchaseOrder.setCancelById(cancelById);
		purchaseOrder.setCancelComments(cancelComments);
		temporaryPurchaseOrderRepository.deactivateGRN(purchaseOrderId);
		temporaryPurchaseOrderRepository.deactivateTemporaryPurchaseOrder(purchaseOrderId);
		temporaryPurchaseOrderRepository.deactivateTemporaryPurchaseItems(purchaseOrderId);

		purchaseOrderRepository.save(purchaseOrder);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "CANCELLED SUCCESSFULLY", null);
	}

	public ResponseEntity<Object> updatePurchaseOrder(Integer purchaseOrderId,
			PurchaseOrderRequestDTOForUpdate purchaseOrderRequestDTO, String jwtToken) {
		PurchaseOrder purchaseOrder = purchaseOrderRepository.getPurchaseOrderById(purchaseOrderId);
		if (purchaseOrder == null) {
			return ResponseHandler.generateResponse(true, HttpStatus.NOT_FOUND, "Record not exists", null);
		}
		JwtDetails jwtDetails = modelMapper.map((ResponseHandler.getUserDetailsByToken(jwtToken)).getBody(),
				JwtDetails.class);
		modelMapper.map(purchaseOrderRequestDTO, purchaseOrder);
		purchaseOrder.setModifiedBy(jwtDetails.getUserId());
		purchaseOrder.setModifiedUsername(jwtDetails.getUserName());
//		purchaseOrder.getPurchaseItems().forEach(item -> item.setPurchaseOrder(purchaseOrder));
		List<PurchaseItems> purcahseItems = setPurchaseOrderAndEnvItemsInStoreIdForUpdate(
				purchaseOrderRequestDTO.getPurchaseItems(), purchaseOrder, jwtDetails);
		purchaseOrder.setPurchaseItems(purcahseItems);
		purchaseOrderRepository.save(purchaseOrder);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

	}

	private List<PurchaseItems> setPurchaseOrderAndEnvItemsInStoreIdForUpdate(
			List<PurchaseItemsForUpdate> purchaseItems, PurchaseOrder purchaseOrder, JwtDetails jwtDetails) {
		List<PurchaseItems> purchaseItemsForUpdate = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(purchaseItems)) {
			purchaseItems.forEach(pi -> {
				PurchaseItems pitfu = modelMapper.map(pi, PurchaseItems.class);
				pitfu.setPurchaseOrder(purchaseOrder);
				pitfu.setEnvItemsInStoresId(envItemsInStoresService.get(pi.getEnvItemsInStoresId()));
				pitfu.setModifiedBy(jwtDetails.getUserId());
				pitfu.setModifiedUsername(jwtDetails.getUserName());
				purchaseItemsForUpdate.add(pitfu);
			});

		}
		return purchaseItemsForUpdate;

	}

	public ResponseEntity<Object> getListOfPOForGRN(DraftOrderRequestDTO draftOrderRequestDTO) {
		Pageable pageable = PageRequest.of(draftOrderRequestDTO.getPageNo(), draftOrderRequestDTO.getPageSize());
		Page<Object> list = purchaseOrderRepository.getListOfPOForGRN(draftOrderRequestDTO, pageable);
		PaginationDTO paginationDTO = new PaginationDTO();
		paginationDTO.setContent(list.getContent());
		paginationDTO.setIslast(list.isLast());
		paginationDTO.setPageNo(draftOrderRequestDTO.getPageNo());
		paginationDTO.setPageSize(draftOrderRequestDTO.getPageSize());
		paginationDTO.setTotalElement(list.getTotalElements());
		paginationDTO.setTotalPage(list.getTotalPages());
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", paginationDTO);

	}

	public ResponseEntity<Object> getPOForGRNByPoNo(String poNo) {
		List<PurchaseItems> purchaseorderItems = purchaseItemsRepository.getPOForGRNByPoNo(poNo);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", purchaseorderItems);
	}

	public ResponseEntity<Object> createGrn(GrnDTO grnDTO, String jwtToken) {
		try {
//			boolean isExists = grnRepository.existsByPurchaseRefNo(grnDTO.getPoNo());
//			if (isExists) {
//				return ResponseHandler.generateResponse(true, HttpStatus.OK,
//						"GRN Already exists with this reference no", null);
//
//			}

			Float totalAmount = (float) grnDTO.getGrnProducts().stream().mapToDouble(ListOfGrnProducts::getValue).sum();

			List<GRN> grnList = new ArrayList<>();
			String grnNo = getGrnNoWithRequestType(grnDTO.getRequestType());
			JwtDetails jwtDetails = modelMapper.map((ResponseHandler.getUserDetailsByToken(jwtToken)).getBody(),
					JwtDetails.class);
			grnDTO.getGrnProducts().stream().forEach(createGrn -> {
				GRN grn = new GRN();
				grn.setInvoiceNumber(grnDTO.getInvoiceNo());
				grn.setInvoiceDate(grnDTO.getInvoiceDate());
				grn.setRemarks(grnDTO.getRemarks());
				grn.setAquistion(grnDTO.getAquistion());
				grn.setInvoiceDescription(createGrn.getDescriptionAsperInvoice());
				grn.setItemDescription(createGrn.getItemDescription());
				grn.setPurchaseRefNo(grnDTO.getPoNo());
				grn.setQuantity(createGrn.getQuantity());
				grn.setPurchaseItemId(createGrn.getPurchaseItemId());
				PurchaseOrder purchaseOrder = purchaseOrderRepository
						.getPurchaseOrderById(createGrn.getPurchaseOrderId());
				grn.setItemName(createGrn.getItemName());
				grn.setVendorId(purchaseOrder.getVendorId());
				grn.setInstituteId(purchaseOrder.getInstituteId());
				grn.setVendorName(purchaseOrder.getVendor());
				grn.setQuantity(createGrn.getQuantity());
				grn.setBalanceQty(createGrn.getBalanceQuantity());
				grn.setEnterQty(createGrn.getEnterQuantity());
				grn.setTotal(totalAmount);
				grn.setGrnType(GRN_TYPE.PO);
				grn.setOriginalTotal(createGrn.getOriginalTotal());
				grn.setGrnNo(grnNo);
				grn.setCreatedBy(jwtDetails.getUserId());
				grn.setCreatedUserName(jwtDetails.getUserName());
				grn.setActive(true);
				grn.setEnvItemId(createGrn.getEnvItemsInStoresId());
				grn.setPurchaseOrderId(createGrn.getPurchaseOrderId());
				grn.setStoreId(grnDTO.getStoreId());
				grn.setStoreName(grnDTO.getStoreName());
				grn.setUom(ObjectUtils.isNotEmpty(getUomForGRN(createGrn.getEnvItemsInStoresId()))
						? getUomForGRN(createGrn.getEnvItemsInStoresId())
						: null);
				grn.setUomShortName(ObjectUtils.isNotEmpty(getUomShortNameForGRN(createGrn.getEnvItemsInStoresId()))
						? getUomShortNameForGRN(createGrn.getEnvItemsInStoresId())
						: null);
				grn.setDiscount(createGrn.getDiscount());
				grn.setDiscountTotal(createGrn.getDiscountTotal());
				grn.setGst(createGrn.getGst());
				grn.setGstTotal(createGrn.getGstTotal());
				grn.setCostTotal(createGrn.getCostTotal());
				grn.setTotalAmount(createGrn.getTotalAmount());
				grn.setItemId(createGrn.getItemId());
				ItemsCreation itemsCreation = itemsCreationRepository.getItemCreationById(createGrn.getItemId());
				grn.setVoucherHeadNewId(
						ObjectUtils.isNotEmpty(itemsCreation) ? itemsCreation.getVoucher_head_new_id() : null);
				grnList.add(grn);
				purchaseOrderRepository.updateGrnCreationStatus(createGrn.getPurchaseOrderId());
				purchaseItemsRepository.balancedQuantityUpdate((createGrn.getBalanceQuantity()).floatValue(),
						createGrn.getPurchaseItemId());

			});

			grnRepository.saveAll(grnList);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", grnNo);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());

		}
	}

	public ResponseEntity<Object> directCreateGrn(GrnDTO grnDTO, String jwtToken) {
		try {

			String grnNo = getGrnNo(grnDTO.getStoreId());
			List<GRN> grnList = new ArrayList<>();
			List<LibraryInventory> libraryInventories = new ArrayList<>();
			JwtDetails jwtDetails = modelMapper.map((ResponseHandler.getUserDetailsByToken(jwtToken)).getBody(),
					JwtDetails.class);
			grnDTO.getDirectGrnDTOs().stream().forEach(createGrn -> {
				if (createGrn.getIsLibrary() == Boolean.FALSE) {
					GRN grn = new GRN();
					grn.setInvoiceNumber(grnDTO.getInvoiceNo());
					grn.setInvoiceDate(grnDTO.getInvoiceDate());
					grn.setRemarks(grnDTO.getRemarks());
					grn.setAquistion(grnDTO.getAquistion());
					grn.setItemDescription(createGrn.getItemDescription());
					grn.setPurchaseRefNo(grnDTO.getPoNo());
					grn.setQuantity(createGrn.getQuantity());
					grn.setItemName(createGrn.getItemName());
					grn.setVendorId(grnDTO.getVendorId());
					grn.setInstituteId(grnDTO.getInstituteId());
					grn.setVendorName(grnDTO.getVendorName());
					grn.setQuantity(createGrn.getQuantity());
					grn.setBalanceQty(createGrn.getBalanceQuantity());
					grn.setEnterQty(createGrn.getEnterQuantity());
					grn.setAttachmentPath(createGrn.getAttachment());
					grn.setTotal(createGrn.getValue());
					grn.setCreatedBy(jwtDetails.getUserId());
					grn.setCreatedUserName(jwtDetails.getUserName());
					grn.setStoreId(grnDTO.getStoreId());
					grn.setStoreName(grnDTO.getStoreName());
					grn.setGrnNo(grnNo);
					grn.setItemId(createGrn.getItemId());
					grn.setEnvItemId(createGrn.getEnvItemId());
					grn.setGrnType(GRN_TYPE.DIRECT_GRN);
					grn.setOriginalTotal(createGrn.getOriginalTotal());
					grn.setActive(true);
					grn.setEnvItemId(createGrn.getEnvItemsInStoresId());
					grn.setPurchaseOrderId(createGrn.getPurchaseOrderId());
					grn.setPurchaseItemId(createGrn.getPurchaseItemId());
					grn.setUom(ObjectUtils.isNotEmpty(getUomForGRN(createGrn.getEnvItemsInStoresId()))
							? getUomForGRN(createGrn.getEnvItemsInStoresId())
							: null);
					grn.setUomShortName(ObjectUtils.isNotEmpty(getUomShortNameForGRN(createGrn.getEnvItemsInStoresId()))
							? getUomShortNameForGRN(createGrn.getEnvItemsInStoresId())
							: null);
					grn.setDiscount(createGrn.getDiscount());
					grn.setDiscountTotal(createGrn.getDiscountTotal());
					grn.setGst(createGrn.getGst());
					grn.setGstTotal(createGrn.getGstTotal());
					grn.setCostTotal(createGrn.getCostTotal());
					grn.setTotalAmount(createGrn.getTotalAmount());
					Vendor vendorDetail = vendorRepository.getVendorById(grnDTO.getVendorId());
					grn.setVoucherHeadNewId(
							ObjectUtils.isNotEmpty(vendorDetail) ? vendorDetail.getVoucher_head_new_id() : null);
					grnList.add(grn);
					purchaseOrderRepository.updateGrnCreationStatus(createGrn.getPurchaseOrderId());
					purchaseItemsRepository.balancedQuantityUpdate((createGrn.getBalanceQuantity()).floatValue(),
							createGrn.getPurchaseItemId());
				} else {
					List<LibraryInventory> libraryInventory = createLibraryInventory(grnDTO, createGrn);
					libraryInventories.addAll(libraryInventory);
				}

			});
			if (ObjectUtils.isNotEmpty(grnList)) {
				grnRepository.saveAll(grnList);

			}

			if (ObjectUtils.isNotEmpty(libraryInventories)) {
				libraryInventoryRepository.saveAll(libraryInventories);
			}
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", grnNo);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());

		}
	}

	private List<LibraryInventory> createLibraryInventory(GrnDTO grnDTO, DirectGrnDTO createGrn) {
		LibraryDetailsDTO libraryDetailsDTO = libraryInventoryRepository.getLibraryBooksDetails(createGrn.getItemId());
		String accessionNumber = libraryInventoryRepository.getLatestAccessionNo();
		Integer q = createGrn.getQuantity().intValue();
		List<LibraryInventory> libraryInventories = new ArrayList<>();
		while (q != 0) {
			if (ObjectUtils.isNotEmpty(libraryDetailsDTO)) {
				LibraryInventory libraryInventory = new LibraryInventory();
				libraryInventory.setAuthor(
						ObjectUtils.isNotEmpty(libraryDetailsDTO.getAuthor()) ? libraryDetailsDTO.getAuthor() : null);
				libraryInventory.setTitle(
						ObjectUtils.isNotEmpty(libraryDetailsDTO.getTitle()) ? libraryDetailsDTO.getTitle() : null);
				libraryInventory.setItemId(createGrn.getItemId());
				libraryInventory.setCreatedBy(grnDTO.getUserId());
				libraryInventory.setCreatedUserName(grnDTO.getUserName());
				libraryInventory.setAccessionNumber(incrementAccessionNumber(accessionNumber));
				libraryInventory
						.setBarcode(libraryInventory.getAccessionNumber() + "/" + incrementBarCode(accessionNumber));
				libraryInventories.add(libraryInventory);

			}
			q--;
		}
		return libraryInventories;

	}

	private String incrementBarCode(String accessionNumber) {
		if (StringUtils.isEmpty(accessionNumber)) {
			return "01";
		}
		int currentNumber = Integer.parseInt(accessionNumber);
		int incrementedNumber = currentNumber + 1;
		return String.format("%02d", incrementedNumber);
	}

	private String incrementAccessionNumber(String currentAccessionNumber) {
		if (StringUtils.isEmpty(currentAccessionNumber)) {
			return "00001";
		}
		int currentNumber = Integer.parseInt(currentAccessionNumber);
		int incrementedNumber = currentNumber + 1;
		return String.format("%05d", incrementedNumber);
	}

	private String getUomForGRN(Integer envItemId) {

		return envItemsInStoresRepository.getUomForGRN(envItemId);
	}

	private String getUomShortNameForGRN(Integer envItemId) {

		return envItemsInStoresRepository.getUomShortNameForGRN(envItemId);
	}

	private String getGrnNo(Integer storeId) {
		String storeShortName = storesStockRepository.getStockTypeShortName(storeId);
		String grnNumber = "";
		if (StringUtils.isNotEmpty(storeShortName)) {
			int currentYear = Year.now().getValue();
			int count = 1;
			String formattedNumber = "";
			GrnNumberConfiguration grnNumberConfiguration = grnNumberConfigurationRepository
					.findByCurrentYear(currentYear);
			if (ObjectUtils.isEmpty(grnNumberConfiguration)) {
				GrnNumberConfiguration grnNumberConfig = new GrnNumberConfiguration();
				formattedNumber = String.format("%04d", count);
				grnNumberConfig.setCounter(count);
				grnNumberConfig.setCurrentYear(currentYear);
				grnNumberConfigurationRepository.save(grnNumberConfig);
			} else {
				count = grnNumberConfiguration.getCounter();
				count++;
				formattedNumber = String.format("%04d", count);
				grnNumberConfiguration.setCounter(count);
				grnNumberConfigurationRepository.save(grnNumberConfiguration);
			}
			int lastTwoDigits = currentYear % 100;
			grnNumber = storeShortName + "/" + lastTwoDigits + "/" + formattedNumber;
		}

		return grnNumber;
	}

	private String getGrnNoWithRequestType(String requestType) {
		String grnNumber = "";
		if (StringUtils.isNotEmpty(requestType)) {
			int currentYear = Year.now().getValue();
			int count = 1;
			String formattedNumber = "";
			GrnNumberConfiguration grnNumberConfiguration = grnNumberConfigurationRepository
					.findByCurrentYear(currentYear);
			if (ObjectUtils.isEmpty(grnNumberConfiguration)) {
				GrnNumberConfiguration grnNumberConfig = new GrnNumberConfiguration();
				formattedNumber = String.format("%04d", count);
				grnNumberConfig.setCounter(count);
				grnNumberConfig.setCurrentYear(currentYear);
				grnNumberConfigurationRepository.save(grnNumberConfig);
			} else {
				count = grnNumberConfiguration.getCounter();
				count++;
				formattedNumber = String.format("%04d", count);
				grnNumberConfiguration.setCounter(count);
				grnNumberConfigurationRepository.save(grnNumberConfiguration);
			}
			int lastTwoDigits = currentYear % 100;
			grnNumber = requestType + "/" + lastTwoDigits + "/" + formattedNumber;
		}

		return grnNumber;
	}

	public ResponseEntity<Object> getListofDirectGRN(DraftOrderRequestDTO draftOrderRequestDTO) {
		try {
			Pageable pageable = PageRequest.of(draftOrderRequestDTO.getPageNo(), draftOrderRequestDTO.getPageSize(),
					Sort.by(draftOrderRequestDTO.getSort()).descending());
			Page<DirectGrnListDTO> list = grnRepository.getListofDirectGRN(draftOrderRequestDTO, pageable);
			Map<String, List<DirectGrnListDTO>> directGrn = list.stream()
					.filter(k -> StringUtils.isNotEmpty(k.getGrnNumber())).collect(Collectors
							.groupingBy(DirectGrnListDTO::getGrnNumber, LinkedHashMap::new, Collectors.toList()));
			List<DirectGrnListDTO> directGrnDTOs = new ArrayList<>();
			directGrn.entrySet().stream().forEach(t -> {
				DirectGrnListDTO directGrnDTO = new DirectGrnListDTO();
				directGrnDTO.setGrnNumber(t.getKey());
				DirectGrnListDTO directGrnListDTO = t.getValue().stream().findFirst().orElse(null);
				double sumOfQuantity = ObjectUtils.isNotEmpty(directGrnListDTO)
						&& ObjectUtils.isNotEmpty(directGrnListDTO.getQuantity()) ? t.getValue().stream().mapToDouble(dgrn ->
						ObjectUtils.isNotEmpty(dgrn.getQuantity()) ? dgrn.getQuantity() : 0).sum() : 0;

				double sumOfBalanceQuantity = ObjectUtils.isNotEmpty(directGrnListDTO)
						&& ObjectUtils.isNotEmpty(directGrnListDTO.getBalanceQuantity()) ? t.getValue().stream().mapToDouble(dgrn ->
						ObjectUtils.isNotEmpty(dgrn.getBalanceQuantity()) ? dgrn.getBalanceQuantity() : 0).sum() : 0;

				double sumOfEnterQuantity = ObjectUtils.isNotEmpty(directGrnListDTO)
						&& ObjectUtils.isNotEmpty(directGrnListDTO.getEnterQuantity()) ? t.getValue().stream().mapToDouble(dgrn ->
						ObjectUtils.isNotEmpty(dgrn.getEnterQuantity()) ? dgrn.getEnterQuantity() : 0).sum() : 0;

				float total = ObjectUtils.isNotEmpty(directGrnListDTO)
						&& ObjectUtils.isNotEmpty(directGrnListDTO.getValue()) ? (float) t.getValue().stream().mapToDouble(dgrn ->
						ObjectUtils.isNotEmpty(dgrn.getValue()) ? dgrn.getValue() : 0).sum()	: 0;

				Double poTotalAmount = ObjectUtils.isNotEmpty(directGrnListDTO) && ObjectUtils.isNotEmpty(directGrnListDTO.getValue()) ?
						ObjectUtils.isNotEmpty(directGrnListDTO.getPoTotalAmount()) ? directGrnListDTO.getPoTotalAmount(): 0.0 : 0.0;

				String poReferenceNo = ObjectUtils.isNotEmpty(directGrnListDTO) && ObjectUtils.isNotEmpty(directGrnListDTO.getValue()) ?
						ObjectUtils.isNotEmpty(directGrnListDTO.getPoReferenceNo()) ? directGrnListDTO.getPoReferenceNo(): null : null;

				Date approvedDate = ObjectUtils.isNotEmpty(directGrnListDTO) && ObjectUtils.isNotEmpty(directGrnListDTO.getValue()) ?
						ObjectUtils.isNotEmpty(directGrnListDTO.getApprovedDate()) ? directGrnListDTO.getApprovedDate() : null : null;

				Integer grnId = ObjectUtils.isNotEmpty(directGrnListDTO) && ObjectUtils.isNotEmpty(directGrnListDTO.getValue()) ?
						ObjectUtils.isNotEmpty(directGrnListDTO.getGrnId()) ? directGrnListDTO.getGrnId() : null : null;

				if (ObjectUtils.isNotEmpty(directGrnListDTO)) {
					directGrnDTO.setCreatedByUserName(
							StringUtils.isNotEmpty(directGrnListDTO.getCreatedByUserName()) ?
							directGrnListDTO.getCreatedByUserName() : null);
					directGrnDTO.setCreatedDate(
							ObjectUtils.isNotEmpty(directGrnListDTO.getCreatedDate()) ?
							directGrnListDTO.getCreatedDate() : null);
					directGrnDTO.setAquistion(
							StringUtils.isNotEmpty(directGrnListDTO.getAquistion()) ? directGrnListDTO.getAquistion() : null);
					directGrnDTO.setAttachment(
							StringUtils.isNotEmpty(directGrnListDTO.getAttachment()) ? directGrnListDTO.getAttachment() : null);
					directGrnDTO.setInvoiceDate(
							ObjectUtils.isNotEmpty(directGrnListDTO.getInvoiceDate())
							? directGrnListDTO.getInvoiceDate() : null);
					directGrnDTO.setInvoiceNo(
							StringUtils.isNotEmpty(directGrnListDTO.getInvoiceNo()) ? directGrnListDTO.getInvoiceNo() : null);
					directGrnDTO.setGrnNumber(
							StringUtils.isNotEmpty(directGrnListDTO.getGrnNumber()) ? directGrnListDTO.getGrnNumber() : null);
					directGrnDTO.setInstituteId(ObjectUtils.isNotEmpty(
							directGrnListDTO.getInstituteId()) ? directGrnListDTO.getInstituteId() : null);
					directGrnDTO.setItemName(
							StringUtils.isNotEmpty(directGrnListDTO.getItemName()) ? directGrnListDTO.getItemName() : null);
					directGrnDTO.setItemDescription(
							StringUtils.isNotEmpty(directGrnListDTO.getItemDescription()) ? directGrnListDTO.getItemDescription() : null);
					directGrnDTO.setStoreId(
							ObjectUtils.isNotEmpty(directGrnListDTO.getStoreId()) ? directGrnListDTO.getStoreId() : null);
					directGrnDTO.setStoreName(
							ObjectUtils.isNotEmpty(directGrnListDTO.getStoreName()) ? directGrnListDTO.getStoreName() : null);
					directGrnDTO.setVendorId(
							ObjectUtils.isNotEmpty(directGrnListDTO.getVendorId()) ? directGrnListDTO.getVendorId() : null);
					directGrnDTO.setVendorName(
							ObjectUtils.isNotEmpty(directGrnListDTO.getVendorName()) ? directGrnListDTO.getVendorName() : null);
					directGrnDTO.setUom(
							ObjectUtils.isNotEmpty(directGrnListDTO.getUom()) ? directGrnListDTO.getUom() : null);
					directGrnDTO.setUomShortName(
							ObjectUtils.isNotEmpty(directGrnListDTO.getUomShortName()) ? directGrnListDTO.getUomShortName() : null);
					directGrnDTO.setRemarks(
							ObjectUtils.isNotEmpty(directGrnListDTO.getRemarks()) ? directGrnListDTO.getRemarks() : null);
					directGrnDTO.setGrnId(grnId);
					directGrnDTO.setQuantity(sumOfQuantity);
					directGrnDTO.setEnterQuantity(sumOfEnterQuantity);
					directGrnDTO.setBalanceQuantity(sumOfBalanceQuantity);
					directGrnDTO.setValue(total);
					directGrnDTO.setPoTotalAmount(poTotalAmount);
					directGrnDTO.setPoReferenceNo(poReferenceNo);
					directGrnDTO.setApprovedDate(approvedDate);
					directGrnDTOs.add(directGrnDTO);

				}
			});
			PaginationDTO paginationDTO = new PaginationDTO();
			paginationDTO.setContent(directGrnDTOs);
			paginationDTO.setIslast(list.isLast());
			paginationDTO.setPageNo(draftOrderRequestDTO.getPageNo());
			paginationDTO.setPageSize(draftOrderRequestDTO.getPageSize());
			paginationDTO.setTotalElement(list.getTotalElements());
			paginationDTO.setTotalPage(list.getTotalPages());
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", paginationDTO);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", e.getMessage());

		}
	}

	private Date parseToDate(String approvedDateString) {
		try {
			// Parse the ISO-8601 string into an Instant using DateTimeFormatter
			Instant instant = Instant.parse(approvedDateString); // ISO 8601 format support
			return Date.from(instant); // Convert Instant to Date
		} catch (Exception e) {
			e.printStackTrace(); // Handle exception or logging
			return null;
		}
}
	public ResponseEntity<Object> getListofDirectGRNById(String grnNo) {
		List<DirectGrnListDTO> directGrnListDTO = grnRepository.getListofDirectGRNById(grnNo);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", directGrnListDTO);
	}

	public ResponseEntity<Object> uploadGrnFile(MultipartFile multipartFile, String grnNO) {
		try {
			List<GRN> grn = grnRepository.findByGrnNo(grnNO);
			if (ObjectUtils.isNotEmpty(grn)) {
				File file = convertMultiPartToFile(multipartFile);
				String fileName = generateFileName(multipartFile);
				grn.stream().forEach(t -> {
					t.setAttachmentPath(LocalDate.now() + "/" + grnNO + "/" + fileName);
					t.setAttachmantPathType(endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/"
							+ grnNO + "/" + fileName);

				});
				uploadFileToS3Bucket(fileName, file, grnNO);
				logger.debug("Message For Attachment", file);
				grnRepository.saveAll(grn);
				file.delete();
			}
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
		} catch (AmazonServiceException ase) {

			logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
			logger.info("Error Message:    " + ase.getMessage());
			logger.info("HTTP Status Code: " + ase.getStatusCode());
			logger.info("AWS Error Code:   " + ase.getErrorCode());
			logger.info("Error Type:       " + ase.getErrorType());
			logger.info("Request ID:       " + ase.getRequestId());
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		} catch (AmazonClientException ace) {
			logger.info("Caught an AmazonClientException: ");
			logger.info("Error Message: " + ace.getMessage());
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		} catch (IOException ioe) {
			logger.info("IOE Error Message: " + ioe.getMessage());
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}

	}

	private void uploadFileToS3Bucket(String fileName, File file, String grnNO) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + grnNO + "/" + fileName;
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));

	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convertFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convertFile);
		fos.write(file.getBytes());
		fos.close();
		return convertFile;

	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	public byte[] downloadFile(final String keyName) throws NoSuchFileException {
		try {
			byte[] content;
			final S3Object s3Object = s3client.getObject(bucketName, value + "/" + keyName);
			final S3ObjectInputStream stream = s3Object.getObjectContent();
			content = IOUtils.toByteArray(stream);
			System.out.println(content);
			s3Object.close();
			return content;

		} catch (AmazonS3Exception e) {
			if (e.getStatusCode() == org.apache.http.HttpStatus.SC_NOT_FOUND) {
				throw new NoSuchFileException("File Not Found");
			}
			throw new AmazonClientException("", e);
		} catch (IOException | AmazonClientException ex) {
			throw new AmazonClientException("", ex);
		}
	}

	public ResponseEntity<Object> saveStockIssue(List<StockIssueDTO> stockIssues) {
		try {
			List<StockIssue> stockIssuesList = new ArrayList<>();

			String stockNumber = getStoreNo();
			stockIssues.stream().forEach(stock -> {
				StockIssue stockIssue = new StockIssue();
				stockIssue.setItemId(stock.getItemId());
				stockIssue.setItemName(stock.getItemName());
				stockIssue.setDescription(stock.getDescription());
				stockIssue.setAvailableQuantity(stock.getAvailableQuantity());
				stockIssue.setIssueQuantity(stock.getIssueQuantity());
				stockIssue.setPurpose(stock.getPurpose());
				stockIssue.setStoreId(stock.getStoreId());
				stockIssue.setStoreName(stock.getStoreName());
				stockIssue.setStocks(stock.getStocks());
				stockIssue.setCreatedBy(stock.getCreatedBy());
				stockIssue.setCreatedByUserName(stock.getCreatedByUserName());
				stockIssue.setEndUserId(stock.getEndUserId());
				stockIssue.setEndUserName(stock.getEndUserName());
				stockIssue.setStockNo(stockNumber);
				stockIssue.setUom(stock.getUom());
				stockIssuesList.add(stockIssue);

			});
			stockIssueRepository.saveAll(stockIssuesList);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", stockNumber);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}

	}

	private String getStoreNo() {
		int currentYear = Year.now().getValue();
		int count = 1;
		String formattedNumber = "";
		StockNumberConfiguration stockNumberConfiguration = stockNumberConfigurationRepository
				.findByCurrentYear(currentYear);

		if (ObjectUtils.isEmpty(stockNumberConfiguration)) {
			StockNumberConfiguration stockNumberConfig = new StockNumberConfiguration();
			formattedNumber = String.format("%04d", count);
			stockNumberConfig.setCurrentYear(currentYear);
			stockNumberConfig.setCounter(count);
			stockNumberConfigurationRepository.save(stockNumberConfig);

		} else {
			count = stockNumberConfiguration.getCounter();
			count++;
			formattedNumber = String.format("%04d", count);
			stockNumberConfiguration.setCounter(count);
			stockNumberConfigurationRepository.save(stockNumberConfiguration);

		}
		int lastTwoDigits = currentYear % 100;
		String stockNumber = lastTwoDigits + "/" + formattedNumber;
		return stockNumber;
	}

	public ResponseEntity<Object> getListOfStockIssue(DraftOrderRequestDTO draftOrderRequestDTO) {
		Pageable pageable = PageRequest.of(draftOrderRequestDTO.getPageNo(), draftOrderRequestDTO.getPageSize(),
				Sort.by("createdDate").descending());
		Page<StockIssuesByStockNo> purchaseOrders = stockIssueRepository.getStockIssue(pageable);
		PaginationDTO paginationDTO = new PaginationDTO();
		paginationDTO.setContent(purchaseOrders.getContent());
		paginationDTO.setIslast(purchaseOrders.isLast());
		paginationDTO.setPageNo(draftOrderRequestDTO.getPageNo());
		paginationDTO.setPageSize(draftOrderRequestDTO.getPageSize());
		paginationDTO.setTotalElement(purchaseOrders.getTotalElements());
		paginationDTO.setTotalPage(purchaseOrders.getTotalPages());
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", paginationDTO);
	}

	public ResponseEntity<Object> getListOfStockRegister(DraftOrderRequestDTO draftOrderRequestDTO) {
		try {
			Pageable pageable = PageRequest.of(draftOrderRequestDTO.getPageNo(), draftOrderRequestDTO.getPageSize(),
					Sort.by("created_date").descending());
			Page<EnvItemsInStores> itemCreations = envItemsInStoresRepository.getItemAssigment(pageable);
			PaginationDTO paginationDTO = new PaginationDTO();

			List<StockRegisterDTO> stockRegisterDTOs = new ArrayList<>();

			itemCreations.getContent().stream().forEach(item -> {
				StockRegisterDTO stockRegisterDTO = new StockRegisterDTO();
				stockRegisterDTO.setItemAssignmentId(item.getEnv_item_id());
				stockRegisterDTO.setItemAssigmentName(item.getItem_assigment_name());
				stockRegisterDTO.setItemId(item.getItem_id());
				stockRegisterDTO.setItemName(
						ObjectUtils.isNotEmpty(getItemName(item.getItem_id())) ? getItemName(item.getItem_id()) : null);
				stockRegisterDTO.setItemDescription(
						ObjectUtils.isNotEmpty(item.getItem_description()) && ObjectUtils.isNotEmpty(item.getMake())
								? item.getItem_description() + "-" + item.getMake()
								: null);
				stockRegisterDTO.setLegderId(item.getLedger_id());
				stockRegisterDTO.setStockIssue(ObjectUtils.isNotEmpty(getStockQuantity(item.getEnv_item_id()))
						?  getStockQuantity(item.getEnv_item_id())
						: 0);
				stockRegisterDTO.setGrn(ObjectUtils.isNotEmpty(getGrnQuantity(item.getEnv_item_id()))
						? getGrnQuantity(item.getEnv_item_id())
						: 0);
				stockRegisterDTO.setScrap(ObjectUtils.isNotEmpty(item.getScrap()) ? item.getScrap() : 0);
				Double openingBalance = ObjectUtils.isNotEmpty(item.getOpening_balance()) ? item.getOpening_balance()
						: 0;
				Double scrap = ObjectUtils.isNotEmpty(item.getScrap()) ? item.getScrap() : 0;

				Double closingStock = openingBalance + stockRegisterDTO.getGrn() - stockRegisterDTO.getStockIssue()
						- scrap;
				stockRegisterDTO.setClosingStock(closingStock);
				stockRegisterDTO.setOpeningStock(openingBalance);
				stockRegisterDTO.setScrap(scrap);
				UomDTO uomDTO = measureRepository.getUomByMeasureId(item.getMeasure_id());
				stockRegisterDTO.setUom(ObjectUtils.isNotEmpty(uomDTO) ? uomDTO.getUomName() : null);

				stockRegisterDTOs.add(stockRegisterDTO);

			});

			paginationDTO.setContent(stockRegisterDTOs);
			paginationDTO.setIslast(itemCreations.isLast());
			paginationDTO.setPageNo(draftOrderRequestDTO.getPageNo());
			paginationDTO.setPageSize(draftOrderRequestDTO.getPageSize());
			paginationDTO.setTotalElement(itemCreations.getTotalElements());
			paginationDTO.setTotalPage(itemCreations.getTotalPages());
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", paginationDTO);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());

		}
	}

	private String getStockUom(Integer item_id) {

		return stockIssueRepository.getStockUom(item_id);
	}

	private String getGrnUom(Integer item_id) {
		return grnRepository.getGrnUom(item_id);
	}

	private String getItemName(Integer item_id) {
		return itemsCreationRepository.getItemNameByItemId(item_id);
	}

	private Double getStockQuantity(Integer envItemId) {
		return storeIndentRequestRepository.sumQuantityByItemName(envItemId);
	}

	private Double getGrnQuantity(Integer envItemId) {
		return grnRepository.sumQuantityByItemName(envItemId);
	}

	public ResponseEntity<Object> saveScrap(ScrapDTO scrapDTO) {
		EnvItemsInStores itemAssignment = envItemsInStoresRepository.findById(scrapDTO.getItemAssigmentId()).get();
		itemAssignment.setScrap(scrapDTO.getEnterQuantity());
		itemAssignment.setScrap_remarks(scrapDTO.getRemarks());
		itemAssignment.setScrap_uom(scrapDTO.getUom());
		envItemsInStoresRepository.save(itemAssignment);

		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
	}

	public ResponseEntity<Object> getClosingStock(Integer item_assigment_id) {
		EnvItemsInStores itemAssignment = envItemsInStoresRepository.findById(item_assigment_id).get();
		Double stockQuantity = ObjectUtils.isNotEmpty(getStockQuantity(itemAssignment.getEnv_item_id()))
				? getStockQuantity(itemAssignment.getEnv_item_id())
				: 0;
		Double grnQuantity = ObjectUtils.isNotEmpty(getGrnQuantity(itemAssignment.getEnv_item_id()))
				? getGrnQuantity(itemAssignment.getEnv_item_id())
				: 0;
		Double openingBalance = ObjectUtils.isNotEmpty(itemAssignment.getOpening_balance())
				? itemAssignment.getOpening_balance()
				: 0;
		Double scrap = ObjectUtils.isNotEmpty(itemAssignment.getScrap()) ? itemAssignment.getScrap() : 0;
		Double closingStock = openingBalance + grnQuantity - stockQuantity - scrap;
		ClosingStockDTO closingStockDTO = new ClosingStockDTO();
		closingStockDTO.setClosingStock(closingStock);
		UomDTO uomDTO = measureRepository.getUomByMeasureId(itemAssignment.getMeasure_id());
		closingStockDTO.setUom(uomDTO.getUomName());
		closingStockDTO.setUomShortName(uomDTO.getUomShortName());
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", closingStockDTO);
	}

	public ResponseEntity<Object> getGrnByItemAssigmentId(Integer item_assigment_id) {
		try {
			List<GrnByItemAssigmentDTO> grnByItemAssigmentDTOs = grnRepository
					.getGrnByItemAssigmentId(item_assigment_id);
			grnByItemAssigmentDTOs.stream().forEach(t -> {
				t.setItemAssignmentName(
						ObjectUtils.isNotEmpty(t.getItemDescription()) && ObjectUtils.isNotEmpty(t.getMake())
								? t.getItemDescription() + "-" + t.getMake()
								: null);
			});
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", grnByItemAssigmentDTOs);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}

	public ResponseEntity<Object> getStockIssueByItemAssigmentId(Integer item_assigment_id) {
		try {
			List<StockIssueByItemAssigmentDTO> stockIssueByItemAssigmentDTOs = stockIssueRepository
					.getStockIssueByItemAssigmentId(item_assigment_id);
			stockIssueByItemAssigmentDTOs.stream().forEach(t -> {
				t.setItemAssignmentName(
						ObjectUtils.isNotEmpty(t.getItemDescription()) && ObjectUtils.isNotEmpty(t.getMake())
								? t.getItemDescription() + "-" + t.getMake()
								: null);
			});
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", stockIssueByItemAssigmentDTOs);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}

	public ResponseEntity<Object> getStockIssueByStockNumber(String stockNumber) {
		try {
			List<StockIssue> stockIssues = stockIssueRepository.findByStockNo(stockNumber);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", stockIssues);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}

	public ResponseEntity<Object> getScrapDetails(Integer itemAssignmentId) {
		try {
			EnvItemsInStores itemAssignment = envItemsInStoresRepository.findById(itemAssignmentId).get();
			ScrapDTO scrapDTO = new ScrapDTO();
			scrapDTO.setEnterQuantity(
					ObjectUtils.isNotEmpty(itemAssignment.getScrap()) ? itemAssignment.getScrap() : null);
			scrapDTO.setRemarks(
					ObjectUtils.isNotEmpty(itemAssignment.getScrap_remarks()) ? itemAssignment.getScrap_remarks()
							: null);
			scrapDTO.setUom(
					ObjectUtils.isNotEmpty(itemAssignment.getScrap_uom()) ? itemAssignment.getScrap_uom() : null);
			scrapDTO.setItemAssigmentId(itemAssignmentId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", scrapDTO);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}

	}

	public ResponseEntity<Object> getStores() {
		try {
			List<StoreDTO> storeDTOs = storesStockRepository.getStores();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", storeDTOs);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}

	}

	public ResponseEntity<Object> getListOfStockRegisterByStoreId(Integer storeId) {
		try {

			List<EnvItemsInStores> itemCreations = envItemsInStoresRepository.getListOfStockRegisterByStoreId(storeId);

			List<StockRegisterDTO> stockRegisterDTOs = new ArrayList<>();

			itemCreations.stream().forEach(item -> {
				StockRegisterDTO stockRegisterDTO = new StockRegisterDTO();
				stockRegisterDTO.setItemAssignmentId(item.getEnv_item_id());
				stockRegisterDTO.setItemAssigmentName(item.getItem_assigment_name());
				stockRegisterDTO.setItemId(item.getItem_id());
				stockRegisterDTO.setItemName(
						ObjectUtils.isNotEmpty(getItemName(item.getItem_id())) ? getItemName(item.getItem_id()) : null);
				stockRegisterDTO.setItemDescription(
						ObjectUtils.isNotEmpty(item.getItem_description()) && ObjectUtils.isNotEmpty(item.getMake())
								? item.getItem_description() + "-" + item.getMake()
								: null);
				stockRegisterDTO.setLegderId(item.getLedger_id());
				stockRegisterDTO.setStockIssue(ObjectUtils.isNotEmpty(getStockQuantity(item.getEnv_item_id()))
						? getStockQuantity(item.getEnv_item_id())
						: 0);
				stockRegisterDTO.setGrn(ObjectUtils.isNotEmpty(getGrnQuantity(item.getEnv_item_id()))
						? getGrnQuantity(item.getEnv_item_id())
						: 0);
				stockRegisterDTO.setScrap(ObjectUtils.isNotEmpty(item.getScrap()) ? item.getScrap() : 0);
				Double openingBalance = ObjectUtils.isNotEmpty(item.getOpening_balance()) ? item.getOpening_balance()
						: 0;
				Double scrap = ObjectUtils.isNotEmpty(item.getScrap()) ? item.getScrap() : 0;

				Double closingStock = openingBalance + stockRegisterDTO.getGrn() - stockRegisterDTO.getStockIssue()
						- scrap;
				stockRegisterDTO.setClosingStock(closingStock);
				stockRegisterDTO.setOpeningStock(openingBalance);
				stockRegisterDTO.setScrap(scrap);
				UomDTO uomDTO = measureRepository.getUomByMeasureId(item.getMeasure_id());
				stockRegisterDTO.setUom(ObjectUtils.isNotEmpty(uomDTO) ? uomDTO.getUomName() : null);

				stockRegisterDTOs.add(stockRegisterDTO);

			});

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", stockRegisterDTOs);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());

		}
	}

	public ResponseEntity<Object> getDayWiseSummary(Integer itemAssignmentId) {
		try {
			EnvItemsInStores envItemsInStores = envItemsInStoresRepository.getEnvItemsbyId(itemAssignmentId);
			List<GrnByItemAssigmentDTO> grns = grnRepository.getGrnByItemAssigmentId(itemAssignmentId);
			
			List<StockIssueByItemAssigmentDTO> stockIssues = Collections.emptyList();
			try {
				stockIssues = stockIssueRepository.getStockIssueByItemAssigmentId(itemAssignmentId);
			}catch (Exception e){
				e.printStackTrace();
			}
            List<DayWiseStockRegisterDTO> stockRegisterDTOs = new ArrayList<>();
			List<StockEntry> stockEntries = new ArrayList<>();

			SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");

			if (ObjectUtils.isNotEmpty(grns)) {
				grns.forEach(grn -> stockEntries.add(new StockEntry(grn.getGrnDate(), grn, null)));
			}

			if (ObjectUtils.isNotEmpty(stockIssues)) {
				stockIssues.forEach(
						stockIssue -> stockEntries.add(new StockEntry(stockIssue.getIssueDate(), null, stockIssue)));
			}

			if(ObjectUtils.isNotEmpty(stockEntries)) {
				Collections.sort(stockEntries, Comparator.comparing(StockEntry::getDate));		
			}
		
			stockEntries.forEach(stockEntry -> {
				GrnByItemAssigmentDTO grn = stockEntry.getGrn();
				StockIssueByItemAssigmentDTO stockIssue = stockEntry.getStockIssue();
				DayWiseStockRegisterDTO stockRegisterDTO = new DayWiseStockRegisterDTO();
				stockRegisterDTO.setDate(ObjectUtils.isNotEmpty(stockEntry.getDate()) ? stockEntry.getDate() : null);
				stockRegisterDTO.setGrn(
						ObjectUtils.isNotEmpty(grn) && ObjectUtils.isNotEmpty(grn.getQuantity()) ? grn.getQuantity()
								: 0);
				stockRegisterDTO.setStockIssue(
						(double) (ObjectUtils.isNotEmpty(stockIssue) && ObjectUtils.isNotEmpty(stockIssue.getIssuedQuantity())
								? stockIssue.getIssuedQuantity()
								: 0));
				stockRegisterDTO.setOpeningBalance(ObjectUtils.isNotEmpty(envItemsInStores.getOpening_balance())
						? envItemsInStores.getOpening_balance()
						: 0);
				Double scrap = ObjectUtils.isNotEmpty(envItemsInStores.getScrap()) ? envItemsInStores.getScrap() : 0;

				Double closingStock = stockRegisterDTO.getOpeningBalance() + stockRegisterDTO.getGrn()
						- stockRegisterDTO.getStockIssue() - scrap;
				stockRegisterDTO.setClosingStock(closingStock);
				stockRegisterDTO.setScrap(scrap);
				stockRegisterDTO.setUom(
						ObjectUtils.isNotEmpty(grn) && ObjectUtils.isNotEmpty(grn.getUom()) ? grn.getUom() : null);
				stockRegisterDTOs.add(stockRegisterDTO);
			});

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", stockRegisterDTOs);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());

		}
	}

	public ResponseEntity<Object> directCreateGrnForLibrary(GrnDTO grnDTO) {
		LibraryEvent libraryEvent = new LibraryEvent(grnDTO);
		applicationEventPublisher.publishEvent(libraryEvent);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
	}

	public ResponseEntity<Object> getApprovers() {
		try {
			List<AssignUserResponseDTO> assignUserResponseDTOs = approverCreationRepository.getApprovers();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", assignUserResponseDTOs);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}

	public void purchaseOrderUploadFile(MultipartFile multipartFile, Integer purchaseOrderId) {
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			String purchaseOrderFilePath = LocalDate.now() + pathSlash + purchaseOrderId + pathSlash + fileName;
			uploadFileToS3Bucket(fileName, file, purchaseOrderId);
			purchaseOrderRepository.updationOfPurchaseOrderFilePath(purchaseOrderFilePath, purchaseOrderId);
			file.delete();
		} catch (AmazonServiceException ase) {

			logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
			logger.info("Error Message:    " + ase.getMessage());
			logger.info("HTTP Status Code: " + ase.getStatusCode());
			logger.info("AWS Error Code:   " + ase.getErrorCode());
			logger.info("Error Type:       " + ase.getErrorType());
			logger.info("Request ID:       " + ase.getRequestId());

		} catch (AmazonClientException ace) {
			logger.info("Caught an AmazonClientException: ");
			logger.info("Error Message: " + ace.getMessage());
		} catch (IOException ioe) {
			logger.info("IOE Error Message: " + ioe.getMessage());

		}
	}

	private void uploadFileToS3Bucket(String fileName, File file, Integer purchaseOrderId) {
		final String uniqueFileName = value + pathSlash + LocalDate.now() + pathSlash + purchaseOrderId + pathSlash
				+ fileName;
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));

	}

	public ResponseEntity<Object> getDeactivateApprovedPurchaseOrder(DraftOrderRequestDTO draftOrderRequestDTO) {
		Sort sorted = Sort.by(Direction.DESC, draftOrderRequestDTO.getSort());
		Pageable pageable = PageRequest.of(draftOrderRequestDTO.getPageNo(), draftOrderRequestDTO.getPageSize(), sorted);
		List<Map<String, Object>> purchaseOrdersList = purchaseOrderRepository.getDeactivateApprovedPurchaseOrder(draftOrderRequestDTO, pageable);
		PageImpl<Map<String, Object>> purchaseOrders = new PageImpl<>(purchaseOrdersList, pageable, purchaseOrdersList.size());
		PaginationDTO paginationDTO = new PaginationDTO();
		paginationDTO.setContent(purchaseOrders.getContent());
		paginationDTO.setIslast(purchaseOrders.isLast());
		paginationDTO.setPageNo(draftOrderRequestDTO.getPageNo());
		paginationDTO.setPageSize(draftOrderRequestDTO.getPageSize());
		paginationDTO.setTotalElement(purchaseOrders.getTotalElements());
		paginationDTO.setTotalPage(purchaseOrders.getTotalPages());
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", paginationDTO);
	}

	public ResponseEntity<Object> getListofGRNForPdf(String grnNo) {
		Map<String, Object> detailsForPdf = new HashMap<>();
		List<GrnListDTO> grnListDTO = grnRepository.getListofGRNForPdf(grnNo);
		Map<String, Object> vendor = vendorRepository
				.getFullDetailOfVendorById(ObjectUtils.isNotEmpty(grnListDTO) ? grnListDTO.get(0).getVendorId() : null);
		detailsForPdf.put("grnListDTO", grnListDTO);
		detailsForPdf.put("vendor", vendor);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", detailsForPdf);
	}

	public void temporaryPurchaseOrderUploadFile(MultipartFile multipartFile, Integer temporary_purchase_order_id) {
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			String temporaryPurchaseOrderFilePath = LocalDate.now() + pathSlash + temporary_purchase_order_id
					+ pathSlash + fileName;
			uploadFileToS3Bucket(fileName, file, temporary_purchase_order_id);
			temporaryPurchaseOrderRepository.updationOfTemporaryPurchaseOrderFilePath(temporaryPurchaseOrderFilePath,
					temporary_purchase_order_id);
			file.delete();
		} catch (AmazonServiceException ase) {

			logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
			logger.info("Error Message:    " + ase.getMessage());
			logger.info("HTTP Status Code: " + ase.getStatusCode());
			logger.info("AWS Error Code:   " + ase.getErrorCode());
			logger.info("Error Type:       " + ase.getErrorType());
			logger.info("Request ID:       " + ase.getRequestId());

		} catch (AmazonClientException ace) {
			logger.info("Caught an AmazonClientException: ");
			logger.info("Error Message: " + ace.getMessage());
		} catch (IOException ioe) {
			logger.info("IOE Error Message: " + ioe.getMessage());

		}
	}

	private void uploadFileToS3Bucket1(String fileName, File file, Integer temporary_purchase_order_id) {
		final String uniqueFileName = value + pathSlash + LocalDate.now() + pathSlash + temporary_purchase_order_id
				+ pathSlash + fileName;
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));

	}

	public byte[] temporaryPurchaseOrderFileDownload(final String keyName) throws NoSuchFileException {
		try {
			byte[] content;
			final S3Object s3Object = s3client.getObject(bucketName, value + "/" + keyName);
			final S3ObjectInputStream stream = s3Object.getObjectContent();
			content = IOUtils.toByteArray(stream);
			System.out.println(content);
			s3Object.close();
			return content;

		} catch (AmazonS3Exception e) {
			if (e.getStatusCode() == org.apache.http.HttpStatus.SC_NOT_FOUND) {
				throw new NoSuchFileException("File Not Found");
			}
			throw new AmazonClientException("", e);
		} catch (IOException | AmazonClientException ex) {
			throw new AmazonClientException("", ex);
		}
	}

	public ResponseEntity<Object> getListOfStockRegisterByLegderId(Integer ledgerId) {
		try {
			List<Map<String, Object>> itemCreations = envItemsInStoresRepository
					.getListOfStockRegisterByLedgerId(ledgerId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", itemCreations);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());

		}
	}

	public ResponseEntity<Object> getLegderbyGroupId(Integer groupId) {
		try {
			List<Map<String, Object>> ledgers =ledgerRepository.getLegderbyGroupId(groupId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", ledgers);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}

	public ResponseEntity<Object> getGroupsForStockRegister() {
		try {
			List<Map<String, Object>> groups =groupRepository.getGroupsForStockRegister();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", groups);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}


	public TemporaryPurchaseOrder updateBillApprover(purchaseOrderUpdateDto dto, String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

		TemporaryPurchaseOrder temporaryPurchaseOrder = temporaryPurchaseOrderRepository.findById(dto.getTemporary_purchase_order_id())
		.orElseThrow(()-> new ResourceNotFoundException("Employee not found"));
		temporaryPurchaseOrder.setBillApprovedStatus(dto.getBillApprovedStatus());
		temporaryPurchaseOrder.setBillApprovedId(dto.getBillApprovedId());
		temporaryPurchaseOrder.setModifiedBy(jwtDetails.getUserId());
		temporaryPurchaseOrder.setModifiedUsername(jwtDetails.getUserName());
		return temporaryPurchaseOrderRepository.save(temporaryPurchaseOrder);

	}
	
	public ResponseEntity<Object> getPurchaseOrderOnVoucherHeadNewIdAndSchoolId(Integer voucher_head_new_id, Integer school_id) {
		List<PurchaseOrder> data = purchaseOrderRepository.getPurchaseOrderOnVoucherHeadNewIdAndSchoolId(voucher_head_new_id, school_id);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", data);

	}

	public ResponseEntity<Object> getJournalVoucher(String grnNo) {
		try{
			List<Map<String,Object>> list=purchaseOrderRepository.getJournalVoucher(grnNo);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", list);

		}catch(Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE",e.getMessage() );
	
		}
	}
	
//	 public List<Map<String, Object>> indexPageForGrn() {
//	        return grnRepository.indexPageForGrn();
//	    }
	 
//	 public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
//
//			Page<Object> response1 = grnRepository.getAllDataFilteredByKeyword(pageable, keyword );
//			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
//			}
//
//		public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
//
//			Page<Object> response = grnRepository.getAllSortedData(pageable);
//			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
//		}
	
	public ResponseEntity<Object> getAllSortedData(Pageable pageable, LocalDate start, LocalDate end,
			LocalDate minDate) {
		Date startDate = (start != null) ? java.sql.Date.valueOf(start) : null;
		Date endDate = (end != null) ? java.sql.Date.valueOf(end) : null;
		Date minStartDate = (minDate != null) ? java.sql.Date.valueOf(minDate) : null;

		Page<Object> response = grnRepository.getAllSortedData(pageable, startDate, endDate, minStartDate);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, LocalDate start,
			LocalDate end, LocalDate minDate) {
		Date startDate = (start != null) ? java.sql.Date.valueOf(start) : null;
		Date endDate = (end != null) ? java.sql.Date.valueOf(end) : null;
		Date minStartDate = (minDate != null) ? java.sql.Date.valueOf(minDate) : null;

		Page<Object> response = grnRepository.getAllDataFilteredByKeyword(pageable, keyword, startDate, endDate,
				minStartDate);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public LocalDate getMinimumPaymentVoucherDate() {
	    Optional<Date> minCreatedDate = grnRepository.findMinCreatedDate();
	    return minCreatedDate.map((Date date) -> date.toInstant()
	                                                 .atZone(ZoneId.systemDefault())
	                                                 .toLocalDate())
	                         .orElse(LocalDate.of(2024, 1, 1)); // fallback
	}	

		public List<Map<String, Object>> getDraftJournalVoucher(Integer draft_journal_voucher_id) {
			Integer journal_voucher_number = draftJournalVoucherRepository
					.getDraftJournalVoucherData(draft_journal_voucher_id);
			Integer school_id = draftJournalVoucherRepository
					.getDraftJournalVoucherSclId(draft_journal_voucher_id);
			Integer financial_year_id = draftJournalVoucherRepository
					.getDraftJournalVoucherFyId(draft_journal_voucher_id);
			return draftJournalVoucherRepository.getDraftJournalVoucherData(journal_voucher_number,school_id,financial_year_id);
			
		}

		public List<Map<String, Object>> getJournalVoucher(Integer journal_voucher_id) {
			Integer journal_voucher_number = journalVoucherRepository
					.getJournalVoucherData(journal_voucher_id);
			Integer school_id = journalVoucherRepository
					.getJournalVoucherSclId(journal_voucher_id);
			Integer financial_year_id = journalVoucherRepository
					.getJournalVoucherFyId(journal_voucher_id);
			return journalVoucherRepository.getJournalVoucherByVoucherNumber(journal_voucher_number, school_id, financial_year_id);
		}
		
		public List<Map<String, Object>> getPaymentVoucherDetails(Integer payment_voucher_id) {
			Integer payment_voucher_number = paymentVoucherRepository
					.getPaymentVoucherData(payment_voucher_id);
			Integer school_id = paymentVoucherRepository
					.getPaymentVoucherSclId(payment_voucher_id);
			Integer financial_year_id = paymentVoucherRepository
					.getPaymentVoucherFyId(payment_voucher_id);
			return paymentVoucherRepository.getPaymentVoucherData(payment_voucher_number, school_id, financial_year_id);
		
		}

		public Object mailSendToEmployee(Integer empId, Integer vendorId, MultipartFile attachment) throws IOException {

			String employeeEmail = employeeDetailsRepository.getEmployeeEmailId(empId);
			String vendorName = vendorRepository.getVendorName(vendorId);

			String emailcc = "purchase@acharya.ac.in";

			String subject = "Draft PO - " + vendorName;
			// The content of the email
			String content = "Dear Sir/Madam," + "<br/>" + "<br/>"
					+ "Please find the attached Draft Purchase Order for your review. Kindly confirm whether the specifications listed are accurate."
					+ "<br/>" + "<br/>" + "Please Reply only to purchase@acharya.ac.in " + "<br/>" + "<br/>"
					+ "Thank you for your prompt attention to this matter." + "<br/>" + "<br/>" + "Regards<br/>"
					+ "Team ERP <br/> " + "Acharya Institutes <br/> " + "Bangalore <br/> " + "<br/>" + "<br/>"
					+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br> "
					+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> "
					+ "</body></html>";

			// Send mail with the uploaded attachment
			response_handler.sendMailToEmployeeWithCC(employeeEmail, emailcc, content, subject, attachment);
			return null;
		}

	public void sendMailToVendor(Integer vendorId, String poNumber, MultipartFile attachment) throws IOException {

		String  vendorEmail = vendorRepository.getVendorEmail(vendorId);

		List<String> ccEmails = new ArrayList<>();
		ccEmails.add("stores@acharya.ac.in");
		ccEmails.add("purchase@acharya.ac.in");

		String subject = "Acharya PO No." + poNumber;
		// The content of the email
		String content = "Dear Sir/Madam," + "<br/>" + "<br/>"
				+ "Please find the enclosed Purchase Order (PO) for your reference. Kindly acknowledge receipt of the same."
				+ "<br/>" + "<br/>" + "For material entry, please ensure it is processed through Gate-1, with the signed and sealed DC/TI by Acharya Security."
				+ "<br/>" + "<br/>" + "Additionally, we kindly request that you inform us in advance prior to dispatching goods, technicians, or any other personnel to our campus. This proactive communication will help ensure smooth operations and allow us to adhere to safety protocols effectively."
				+ "<br/>" + "<br/>" + "Thank you for your attention to these details. We look forward to your acknowledgement and cooperation." + "<br/>" + "<br/>"
				+ "<br/>" + "<br/>" + "Regards <br/>"
				+ "Team ERP <br/> " + "Acharya Institutes <br/> " + "Bangalore <br/> " + "<br/>" + "<br/>"
				+ "</body></html>";

		// Send mail with the uploaded attachment
		response_handler.sendMailToVendor(vendorEmail, ccEmails, content, subject, attachment);
	}

	public ResponseEntity<Object> getPOIdAndRefNum() {
		List<Map<String, Object>> POIdAndRefNumList = purchaseOrderRepository.getActivePOIdAndRefNum();
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", POIdAndRefNumList);
	}

	public List<PurchaseOrder> getPurchaseOrderData() {
		return purchaseOrderRepository.getPurchaseOrderData();
	}

	public void updateBillApprover(GrnDraftJournalVoucherDto dto, String jwtToken) {
		List<GRN> grnDetailBasedOnGrnNo = grnRepository.getGrnDetailBasedOnGrnNo(dto.getGrn_no());
	    if (grnDetailBasedOnGrnNo != null && !grnDetailBasedOnGrnNo.isEmpty()) {
	        for (GRN grn : grnDetailBasedOnGrnNo) {

	            if (dto.getDraft_journal_voucher_id() != null) {
	                grn.setDraft_journal_voucher_id(dto.getDraft_journal_voucher_id());
	            }
	            if (dto.getPaymentVoucherId() != null) {
	                grn.setPaymentVoucherId(dto.getPaymentVoucherId());
	            }
	            if (dto.getJournal_voucher_id() != null) {
	                grn.setJournal_voucher_id(dto.getJournal_voucher_id());
	            }
	            if (dto.getDraft_payment_voucher_id() != null) {
	                grn.setDraft_payment_voucher_id(dto.getDraft_payment_voucher_id());
	            }
	        }

	        grnRepository.saveAll(grnDetailBasedOnGrnNo);
	    } else {
	        throw new ResourceNotFoundException("GRN details not found for GRN number: " + dto.getGrn_no());
	    }
	}
	
	
	public void updatePurchaseOrder(PurchaseOrderDto dto, String jwtToken) {
		PurchaseOrder getPurchaseOrder = purchaseOrderRepository.getPurchaseOrder(dto.getPurchase_order_id());
		if (getPurchaseOrder != null) {
			if (dto.getPayment_voucher_id() != null) {
				getPurchaseOrder.setPayment_voucher_id(dto.getPayment_voucher_id());
			}
			if (dto.getDraft_payment_voucher_id() != null) {
				getPurchaseOrder.setDraft_payment_voucher_id(dto.getDraft_payment_voucher_id());
			}

			purchaseOrderRepository.save(getPurchaseOrder);
		} else {
			throw new ResourceNotFoundException("Purchase Order not found !!!" + dto.getPurchase_order_id());
		}

	}

	public ResponseEntity<Object> getGRNByPOId(Integer poId) {
		List<GRN> grnList = grnRepository.getGRNByPOId(poId);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, grnList);
	}
}
