package coreservlets;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class getAllUsers
 */
@WebServlet("/getAllUsers")
public class getAllUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getAllUsers() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Queries q = new Queries();
		HttpSession session = request.getSession();
		coreservlets.MyConnection con = (coreservlets.MyConnection) session.getAttribute("connection");

		if (con != null) {
			try {

				String user = request.getParameter("userName");

				JSONArray users = new JSONArray();

				PreparedStatement ps;
				ps = con.getConnection().prepareStatement(q.getUsers);
				ResultSet rs;

				ps.setString(1, user);
				ps.setString(2, user);
				ps.setString(3, user);
				rs = ps.executeQuery();

				while (rs.next()) {
					JSONObject userRow = new JSONObject();
					userRow.put("name", rs.getString("FullName"));
					userRow.put("user", rs.getString("username"));
					userRow.put("catagory", rs.getString("Category"));
					users.add(userRow);
				}

				// System.out.print(usersList + "\n");
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(users);

			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
