package communication;

import java.util.List;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auxiliary.Templates;
import model.BDMyUser;
import model.MyUser;
import auxiliary.Codes;

public class EmailListServlet extends HttpServlet 
{
	private static final long serialVersionUID = 4203674193098824226L;

	public EmailListServlet()
	{
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		String action = request.getParameter("action");
		System.out.println("SERVLET ACTION: " + action);

		String name = request.getParameter("name");
		String surname = request.getParameter("surname");
		String email = request.getParameter("email");

		if (action == null) //generate webpage
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");

			PrintWriter writer = response.getWriter();

			String webpage = Templates.html_template;
			writer.println(webpage);

			for (MyUser u: BDMyUser.listUsers()) 
			{
				writer.println("<tr>");
				writer.println("<td>" + u.getName() + "</td>");
				writer.println("<td>" + u.getSurname() + "</td>");
				writer.println("<td>" + u.getEmail() + "</td>");
				writer.println("</tr>");
			}
			writer.println("</tbody></table></div></div></body></html>"); //adding bottom tags left on html template 
		} 
		else 
		{
			ObjectOutputStream oos = new ObjectOutputStream(response.getOutputStream());

			switch (action) 
			{
			case "addUser":
				if (!BDMyUser.emailExists(email)) 
				{
					MyUser u = new MyUser(name, surname, email);
					BDMyUser.insert(u);
					oos.writeInt( Codes.SUCCESS );
				} 
				else
				{
					oos.writeInt( Codes.EMAIL_ALREADY_EXISTS );
				}
				break;

			case "updateUser":
				if (BDMyUser.emailExists(email)) 
				{
					MyUser myUser = BDMyUser.selectUser(email);
					myUser.setName(name);
					myUser.setSurname(surname);
					BDMyUser.update(myUser);
					oos.writeInt( Codes.SUCCESS );
				}
				else 
				{
					oos.writeInt( Codes.EMAIL_NOT_FOUND );
				}
				break;

			case "deleteUser":
				if (BDMyUser.emailExists(email)) 
				{
					MyUser u = BDMyUser.selectUser(email);
					BDMyUser.delete(u);
					oos.writeInt( Codes.SUCCESS );
				} 
				else
				{
					oos.writeInt( Codes.EMAIL_NOT_FOUND );
				}
				break;

			case "listUsers":
				List<MyUser> user_list = BDMyUser.listUsers();
				oos.writeObject(user_list);
				oos.writeInt( Codes.SUCCESS );
				break;

			default:
				break;
			}

			oos.flush();
			oos.close();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		doPost(request, response);
	}

}
