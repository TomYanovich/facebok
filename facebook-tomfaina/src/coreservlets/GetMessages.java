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
 * Servlet implementation class GetMassages
 */
@WebServlet("/GetMessages")
public class GetMessages extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetMessages() {
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
		System.out.println("1");
		if (con == null) {
			System.out.println("2");
			return;
		}
		try {

			String username = request.getParameter("userName");
			JSONArray massagesArray = new JSONArray();

			PreparedStatement ps;
			ps = con.getConnection().prepareStatement(Queries.getMessages);
			ResultSet rs;

			ps.setString(1, username);

			rs = ps.executeQuery();

			while (rs.next()) {
				System.out.println("3");
				JSONObject messageRow = new JSONObject();
				messageRow.put("FirstName", rs.getString("firstName"));
				messageRow.put("LastName", rs.getString("lastName"));
				messageRow.put("content", rs.getString("content"));

				massagesArray.add(messageRow);

			}
			System.out.println("4");
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(massagesArray);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

}
