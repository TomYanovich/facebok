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
 * Servlet implementation class IsMyFriend
 */
@WebServlet("/IsMyFriend")
public class IsMyFriend extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public IsMyFriend() {
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
		System.out.println("GetAllUsers: doPost entered");
		if (con == null) {
			return;
		}
		
		JSONObject isFriend = new JSONObject();
		
		try {

			String currentUserId = request.getParameter("currentUserId");
			String theuser = request.getParameter("theuser");

			PreparedStatement ps;
			ps = con.getConnection().prepareStatement(Queries.isFriend);
			ResultSet rs;

			ps.setString(1, currentUserId);
			ps.setString(2, theuser);
			rs = ps.executeQuery();

			rs.next();
			
			isFriend.put("isFriend", rs.getString("isFriend"));
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(isFriend);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

}
