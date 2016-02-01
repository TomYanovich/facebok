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
 * Servlet implementation class GetOnlineFriends
 */
@WebServlet("/GetOnlineFriends")
public class GetOnlineFriends extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetOnlineFriends() {
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
		if (con == null) {
			return;
		}
		try {
			
			String username = request.getParameter("userName");
			JSONArray usersArray = new JSONArray();
			PreparedStatement ps;
			ps = con.getConnection().prepareStatement(Queries.getOnLineFriends);
			ResultSet rs;

			ps.setString(1, username);
			rs = ps.executeQuery();

			while (rs.next()) {
				JSONObject userRow = new JSONObject();
				userRow.put("FullName", rs.getString(1));
				userRow.put("username", rs.getString(2));
				userRow.put("isOnline", rs.getBoolean(3));

				usersArray.add(userRow);
//				System.out.println("FullName: " + rs.getString(1) + "username: " + rs.getString(2));
			}

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(usersArray);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

}
