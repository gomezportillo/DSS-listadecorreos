package model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class BDMyUser 
{
	private static final String PERSISTENCE_UNIT_NAME = "myuser";

	public static void insert(MyUser u) 
	{
		EntityManager em = createEntityManager();

		if (!emailExists(u.getEmail())) 
		{
			em.getTransaction().begin();
			em.persist(u);
			em.getTransaction().commit();
			em.close();
		}
	}

	public static void update(MyUser u) 
	{
		EntityManager em = createEntityManager();

		if (emailExists(u.getEmail())) 
		{
			Query q = em.createQuery("SELECT u FROM MyUser u WHERE u.email = :email");
			q.setParameter("email", u.getEmail());

			MyUser resultado = (MyUser) q.getSingleResult();
			resultado.setName(u.getName());
			resultado.setSurname(u.getSurname());

			em.getTransaction().begin();
			em.merge(resultado);
			em.getTransaction().commit();
			em.close();
		}
	}

	public static void delete(MyUser u) 
	{
		EntityManager em = createEntityManager();


		if (emailExists(u.getEmail())) 
		{
			Query q = em.createQuery("SELECT u FROM MyUser u WHERE u.email = :email");
			q.setParameter("email", u.getEmail());

			MyUser result = (MyUser) q.getSingleResult();

			em.getTransaction().begin();
			em.remove(result);
			em.getTransaction().commit();
			em.close();
		}
	}

	public static MyUser selectUser(String email) 
	{
		EntityManager em = createEntityManager();

		MyUser result = null;

		if (emailExists(email)) 
		{
			Query q = em.createQuery("SELECT u FROM MyUser u WHERE u.email = :email");
			q.setParameter("email", email);
			result = (MyUser) q.getSingleResult();
			em.close();
		}
		return result;
	}

	public static boolean emailExists(String email) 
	{
		EntityManager em = createEntityManager();

		Query q = em.createQuery("SELECT u FROM MyUser u WHERE u.email = :email");
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

	@SuppressWarnings("unchecked")
	public static List<MyUser> listUsers() 
	{
		EntityManager em = createEntityManager();

		Query query = em.createQuery("SELECT u FROM MyUser u");

		List<MyUser> result = query.getResultList();
		em.close();

		return result;
	}

	private static EntityManager createEntityManager()
	{
		EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		return factory.createEntityManager();
	}
}
