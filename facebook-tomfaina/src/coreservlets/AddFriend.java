package coreservlets;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class AddFriend
 */
@WebServlet("/AddFriend")
public class AddFriend extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddFriend() {
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

			String user = request.getParameter("user");
			String friend = request.getParameter("friend");
			JSONObject data = new JSONObject();

			PreparedStatement ps;
			ps = con.getConnection().prepareStatement(Queries.addFriend);

			ps.setString(1, user);
			ps.setString(2, friend);

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				ps.setString(1, friend);
				ps.setString(2, user);
				rowsAffected = ps.executeUpdate();
				data.put("result", (rowsAffected > 0));

			} else {
				data.put("result", false);
			}

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(data);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

}
