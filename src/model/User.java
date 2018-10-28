package model;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class User implements Serializable {

	private static final long serialVersionUID = 5115446418810478651L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;
	private String name;
	private String surname;
	private String email;

	public User() 
	{
		name = surname = email = "";
	}

	public User(User u)
	{
		name = u.getName();
		surname = u.getSurname();
		email = u.getEmail();
	}

	@Override
	public String toString() 
	{
		return "User: " + this.name + " " + this.surname + " " + this.email;
	}

	public long getId() 
	{
		return id;
	}

	public void setId(long id) 
	{
		this.id = id;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String n)
	{
		this.name = n;
	}

	public String getSurname() 
	{
		return surname;
	}

	public void setSurname(String s) 
	{
		this.surname = s;
	}

	public String getEmail() 
	{
		return email;
	}

	public void setEmail(String e) 
	{
		this.email = e;
	}
}