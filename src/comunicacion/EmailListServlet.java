package comunicacion;

import java.util.List;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BDUser;
import model.User;

@WebServlet("/ListaCorreosServlet")
public class EmailListServlet extends HttpServlet {

	private static final long serialVersionUID = 4203674193098824226L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{
		String action = request.getParameter("action");
		String name = request.getParameter("name");
		String surname = request.getParameter("surname");
		String email = request.getParameter("email");

		if (action == null) 
		{
			response.setContentType("text/html");
			PrintWriter writer = response.getWriter();
			writer.println("<h1>DSS Practise 2: Software Architecture</h1>");
		} 
		else 
		{
			PrintWriter writer = response.getWriter();

			switch (action) 
			{
			case "listUser":
				List<User> userList = BDUser.listarUsuarios();
				ObjectOutputStream objet = new ObjectOutputStream(response.getOutputStream());
				objet.writeObject(userList);
				objet.flush();
				objet.close();
				break;

			case "deleteUser":
				if (BDUser.emailExists(email)) 
				{
					User user = BDUser.selectUser(email);
					BDUser.delete(user);
				} 
				else 
				{
					writer.println("<span> User not found </span>");
				}
				break;

			case "addUser":
				if (!BDUser.emailExists(email)) 
				{
					User user = BDUser.selectUser(email);
					BDUser.insertar(user);
				} 
				else 
				{
					writer.println("<span> El usuario ya existe </span>");
				}
				break;

			case "updateUser":
				if (BDUser.emailExists(email)) 
				{
					User user = BDUser.selectUser(email);
					BDUser.update(user);
				} 
				else 
				{
					writer.println("<span> El usuario no existe </span>");
				}
				break;
			}
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		doPost(request, response);
	}

}
