package com.au.dto;

import java.io.Serializable;
import java.util.List;

import org.hibernate.*;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class IdGenerator implements IdentifierGenerator 
{
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Serializable generate(SessionImplementor si, Object obj) throws HibernateException 
	{
		String sid = "001";
		try
		{
			Session s = (Session)si;
			Transaction tx = s.getTransaction();
			Query q = s.createQuery("from Student_Details s");
			int size = q.list().size();
			if(size!=0)
			{
				Query q1 = s.createQuery("select max(cid) from Student_Details s");
				List l = q1.list();
				System.out.println(l.size());
				Object o = l.get(0);
				System.out.println(o);
				String id = "";
				id = o.toString();
				String p2 = id.substring(2);
				int x = Integer.parseInt(p2);
				x = x + 1;
				if(x<=9)
				{
					sid = "00"+x;
				}
				else if(x<=99)
				{
					sid = "0"+x;
				}
				else if(x<=999)
				{
					sid = ""+x;
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return sid;
	}


}
