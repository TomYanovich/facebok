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
 * Servlet implementation class Search
 */
@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Search() {
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
				
				String prefix = request.getParameter("prefix");
				String userName = request.getParameter("userName");
				JSONArray usersList = new JSONArray();

				PreparedStatement ps;
				ps = con.getConnection().prepareStatement(Queries.getAllUsers);
				ResultSet rs;
				ps.setString(1, prefix + "%");
				ps.setString(2, userName);
				ps.setString(3, prefix + "%");
				ps.setString(4, userName);
				ps.setString(5, userName);

				rs = ps.executeQuery();

				while (rs.next()) {
					JSONObject user = new JSONObject();
					user.put("FullName", rs.getString(1));
					user.put("username", rs.getString(2));
					usersList.add(user);

				}

				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(usersList);

			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
