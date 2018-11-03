package model;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Transient;
import javax.persistence.GenerationType;

@Entity
public class MyUser implements Serializable 
{
	private static final long serialVersionUID = 5115446418810478651L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;
	private String name;
	private String surname;
	private String email;

	@Transient
	private int this_variable_will_not_be_saved;
	
	public MyUser() 
	{
		this.name = this.surname = this.email = "";
	}

	public MyUser(MyUser u) 
	{
		this.name = u.getNname();
		this.surname = u.getSurname();
		this.email = u.getEmail();
	}

	@Override
	public String toString() 
	{
		return "User [nombre=" + this.name + ", apellido=" + this.surname + ", email=" + this.email + "]";
	}

	public long getId() 
	{
		return this.id;
	}

	public void setId(long id) 
	{
		this.id = id;
	}

	public String getNname() 
	{
		return this.name;
	}

	public void setName(String n) 
	{
		this.name = n;
	}

	public String getSurname() 
	{
		return this.surname;
	}

	public void setSurname(String s) 
	{
		this.surname = s;
	}

	public String getEmail() 
	{
		return this.email;
	}

	public void setEmail(String e) 
	{
		this.email = e;
	}
}
