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

import org.json.simple.JSONObject;

/**
 * Servlet implementation class GetProfile
 */
@WebServlet("/GetProfile")
public class GetProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetProfile() {
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

		HttpSession session = request.getSession();
		coreservlets.MyConnection con = (coreservlets.MyConnection) session.getAttribute("connection");
		if (con != null) {
			try {
				String username = request.getParameter("userName");
				System.out.println("username was sent: " + username);
				ResultSet rs;
				PreparedStatement ps;
				ps = con.getConnection().prepareStatement(Queries.getProfile);
				ps.setString(1, username);
				rs = ps.executeQuery();

				if (rs.next()) {
					JSONObject user = new JSONObject();
					user.put("FirstName", rs.getString("firstName"));
					user.put("LastName", rs.getString("lastName"));
					user.put("profile", rs.getString("profilePic"));
					user.put("cover", rs.getString("coverPic"));
					user.put("username", rs.getString("username"));

					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().print(user);
				}

				ps.close();

			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

	}

}
