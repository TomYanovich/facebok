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

import coreservlets.Queries;

/**
 * Servlet implementation class GetComments
 */
@WebServlet("/GetComments")
public class GetComments extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetComments() {
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
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		coreservlets.MyConnection con = (coreservlets.MyConnection) session.getAttribute("connection");

		if (con == null) {
			return;
		}
		
		try {
			String postid = request.getParameter("post");
			JSONArray commentArray = new JSONArray();

			PreparedStatement ps;
			ps = con.getConnection().prepareStatement(Queries.getComments);
			ResultSet rs;

			ps.setInt(1, Integer.parseInt(postid));
			rs = ps.executeQuery();

			while (rs.next()) {
				JSONObject commentRow = new JSONObject();
				commentRow.put("FirstName", rs.getString("firstName"));
				commentRow.put("LastName", rs.getString("lastName"));
				commentRow.put("content", rs.getString("content"));
				commentRow.put("pic", rs.getString("profilePic"));

				commentArray.add(commentRow);

			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(commentArray);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

}
