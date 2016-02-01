package coreservlets;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class PostPost
 */
@WebServlet("/PostPost")
public class PostPost extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PostPost() {
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
			String userid = (String) session.getAttribute("userid");
			PreparedStatement ps;

			ps = con.getConnection().prepareStatement(Queries.PostPost);
			java.sql.Date date = new java.sql.Date(java.util.Calendar.getInstance().getTime().getTime());

			ps.setDate(1, date);
			ps.setString(2, request.getParameter("postData"));
			ps.setString(3, userid);
			int affectedRows = ps.executeUpdate();
			ps.close();

			if (affectedRows == 0){
				return;
			}
			
			Statement st;
			st = con.getConnection().createStatement();
			ResultSet rs = st.executeQuery(Queries.getLastPost);

			rs.next();
			JSONObject lastPost = new JSONObject();
			lastPost.put("postId", rs.getString("postId"));
			lastPost.put("content", rs.getString("content"));

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(lastPost);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

}
