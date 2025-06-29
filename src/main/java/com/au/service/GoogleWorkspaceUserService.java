package com.au.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.DirectoryScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

public class GoogleWorkspaceUserService {
    public static void main(String[] args) {
        try (InputStream credentialsStream = new FileInputStream("path/to/your/service_account_key.json")) {
            
            // Create GoogleCredentials with scoped access
        	   // Load GoogleCredentials with scope
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(credentialsStream)
                    .createScoped(Collections.singleton(DirectoryScopes.ADMIN_DIRECTORY_USER));

            // Wrap credentials with HttpCredentialsAdapter
            HttpCredentialsAdapter requestInitializer = new HttpCredentialsAdapter(credentials);

            // Create Directory service with HttpCredentialsAdapter
            Directory directoryService = new Directory.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(), 
                    requestInitializer) // Pass HttpCredentialsAdapter here
                    .setApplicationName("Acharyaerp")
                    .build();

            // Now you can use the directoryService object for API calls

        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
}

