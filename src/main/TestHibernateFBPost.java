package main;

import java.util.List;

import json.FBPost;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class TestHibernateFBPost {

	public static void main(String[] args) {
		Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        
        
        SessionFactory sessionFactory = configuration.buildSessionFactory(ssrb.build());
        Session session = sessionFactory.openSession();
        System.out.println("Test connection with the database created successfuly.");
		
        session.beginTransaction();

		createFBPost(session);

		queryPerson(session);
	}

	private static void queryPerson(Session session) {
		Query query = session.createQuery("from Person");
		List<FBPost> list = query.list();
		java.util.Iterator<FBPost> iter = list.iterator();
		while (iter.hasNext()) {

			FBPost post = iter.next();
			System.out.println("Post: \"" + post.getMessage() + "\", "
					+ post.getLikeCount() + "\", " + post.getShareCount() + "\", "
					+ post.getSource());

		}

		session.getTransaction().commit();

	}

	public static void createFBPost(Session session) {
		FBPost post = new FBPost();

		post.setMessage("A facebook post message");
		post.setLikeCount(15);
		post.setShareCount(8);
		post.setSource("Dustin Dannenhauer");

		session.save(post);
	}

}
