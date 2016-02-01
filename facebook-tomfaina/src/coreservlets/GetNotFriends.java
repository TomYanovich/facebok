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
 * Servlet implementation class GetNotFriends
 */
@WebServlet("/GetNotFriends")
public class GetNotFriends extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetNotFriends() {
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
				//

				String userName = request.getParameter("userName");
				String connectedUser = request.getParameter("connectedUser");
				JSONArray usersArray = new JSONArray();

				PreparedStatement ps;
				ps = con.getConnection().prepareStatement(Queries.getNotFirends);
				ResultSet rs;

				ps.setString(1, connectedUser);
				ps.setString(2, userName);

				rs = ps.executeQuery();

				while (rs.next()) {
					JSONObject userRow = new JSONObject();
					userRow.put("FullName", rs.getString(1));
					userRow.put("username", rs.getString(2));
					userRow.put("isOnline", rs.getBoolean(3));
					userRow.put("isFriend", rs.getString("isFriend"));

					usersArray.add(userRow);

				}

				// System.out.print(usersList + "\n");
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(usersArray);

			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
