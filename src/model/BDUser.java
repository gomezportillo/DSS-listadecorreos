package model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class BDUser 
{
	private static final String PERSISTENCE_UNIT_NAME = "user";
	private static EntityManagerFactory factory;

	public static void insertar(User u) 
	{
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();

		if ( ! emailExists(u.getEmail()) ) 
		{
			em.getTransaction().begin();
			em.persist(u);
			em.getTransaction().commit();
			em.close();
		}
	}

	public static void update(User u) 
	{
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();

		if ( emailExists(u.getEmail()) ) 
		{
			Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email");
			q.setParameter("email", u.getEmail());

			User result = (User) q.getSingleResult();
			result.setName(u.getName());
			result.setSurname(u.getSurname());

			em.getTransaction().begin();
			em.merge(result);
			em.getTransaction().commit();
			em.close();
		}
	}

	public static void delete(User u) 
	{
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();

		if ( emailExists(u.getEmail()) ) 
		{
			Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email");
			q.setParameter("email", u.getEmail());

			User result = (User) q.getSingleResult();

			em.getTransaction().begin();
			em.remove(result);
			em.getTransaction().commit();			
			em.close();
		}
	}

	public static User selectUser(String email)
	{
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();

		User result = null;

		if ( emailExists(email) ) 
		{
			Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email");
			q.setParameter("email", email);
			result = (User) q.getSingleResult();
			em.close();
		}
		return result;
	}

	public static boolean emailExists(String email) 
	{
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();

		Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email");
		q.setParameter("email", email);

		try 
		{
			q.getSingleResult();
			return true;
		} 
		catch (NoResultException e)
		{
			return false;
		}
		finally 
		{
			em.close();
		}
	}

	public static List<User> listarUsuarios() 
	{
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();

		Query q = em.createQuery("SELECT u FROM Usuario u");

		@SuppressWarnings("unchecked")
		List<User> result = q.getResultList();
		em.close();

		return result;
	}
}