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

import modelo.BDUser;
import modelo.User;

@WebServlet("/ListaCorreosServlet")
public class ListaCorreosServlet extends HttpServlet {

	private static final long serialVersionUID = 4203674193098824226L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{
		String accion = request.getParameter("action");
		String name = request.getParameter("nombre");
		String surname = request.getParameter("apellido");
		String email = request.getParameter("email");

		if (accion == null) 
		{
			response.setContentType("text/html");
			PrintWriter writer = response.getWriter();
			writer.println("<h1>Practica 2 de DSS: Arquitecturas Software</h1>");
		} 
		else 
		{
			PrintWriter writer2 = response.getWriter();

			switch (accion) 
			{
			case "listarUsuario":
				List<User> listaUsuarios = BDUser.listarUsuarios();
				ObjectOutputStream objeto = new ObjectOutputStream(response.getOutputStream());
				objeto.writeObject(listaUsuarios);
				objeto.flush();
				objeto.close();
				break;
			case "eliminarUsuario":
				if (BDUser.existeEmail(email)) 
				{
					User user = BDUser.seleccionarUsuario(email);
					BDUser.eliminar(user);
				} 
				else 
				{
					writer2.println("<span> Usuario no encontrado </span>");
				}
				break;
			case "aniadirUsuario":
				if (!BDUser.existeEmail(email)) 
				{
					User user = BDUser.seleccionarUsuario(email);
					BDUser.insertar(user);
				} 
				else 
				{
					writer2.println("<span> El usuario ya existe </span>");
				}
				break;
			case "actualizarUsuario":
				if (BDUser.existeEmail(email)) 
				{
					User user = BDUser.seleccionarUsuario(email);
					BDUser.actualizar(user);
				} 
				else 
				{
					writer2.println("<span> El usuario no existe </span>");
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
