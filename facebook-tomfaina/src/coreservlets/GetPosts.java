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
 * Servlet implementation class GetPosts
 */
@WebServlet("/GetPosts")
public class GetPosts extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetPosts() {
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
		coreservlets.MyConnection con = null;
		try {
			con = new coreservlets.MyConnection();
			if (session.getAttribute("connection") != null) {
				con = (coreservlets.MyConnection) session.getAttribute("connection");
			} else {
				con = new coreservlets.MyConnection();
				session.setAttribute("connection", con);
			}
		} catch (Exception ex) {

		}

		if (con != null) {
			try {

				JSONArray postArray = new JSONArray();
				String userName = request.getParameter("userName");
				String existingpost = request.getParameter("postIds");
				PreparedStatement ps;
				ResultSet rs;

				// get all friends posts
				if (existingpost == null) {
					System.out.println("1");
					ps = con.getConnection().prepareStatement(Queries.getPosts);
					ps.setString(1, userName); // ignore the user from th list
												// in users
					ps.setString(2, userName); // <<added this

					rs = ps.executeQuery();

					while (rs.next()) {
						JSONObject postRow = new JSONObject();
						postRow.put("FullName", rs.getString(1));
						postRow.put("profilePic", rs.getString(2));
						postRow.put("postId", rs.getInt(3));
						postRow.put("date", rs.getString(4));
						postRow.put("content", rs.getString(5));
						postRow.put("author", rs.getString(6));
						postRow.put("test","1" );

						postArray.add(postRow);

					}
					
				} else {
					// get only new post
					System.out.println("2");
					ps = con.getConnection().prepareStatement(Queries.getNewPosts);
					ps.setString(1, userName); // ignore the user from the list
												// in users
					ps.setString(2, userName); // <<added this
					ps.setString(3, existingpost); // the ids of the existing
													// posts in DB
					
					rs = ps.executeQuery();

					while (rs.next()) {
						JSONObject postRow = new JSONObject();
						postRow.put("FullName", rs.getString(1));
						postRow.put("profilePic", rs.getString(2));
						postRow.put("postId", rs.getInt(3));
						postRow.put("date", rs.getString(4));
						postRow.put("content", rs.getString(5));
						postRow.put("author", rs.getString(6));
						postRow.put("test","2" );

						postArray.add(postRow);

					}
				}

				

				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(postArray);

			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
