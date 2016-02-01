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
 * Servlet implementation class PostComment
 */
@WebServlet("/PostComment")
public class PostComment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PostComment() {
		super();
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
				String postid = request.getParameter("post");
				String content = request.getParameter("content");
				String user = request.getParameter("userName");
				JSONObject data = new JSONObject();

				PreparedStatement ps;
				ps = con.getConnection().prepareStatement(Queries.postComment);

				ps.setInt(1, Integer.parseInt(postid));
				ps.setString(2, content);
				ps.setString(3, user);

				int rowsAffected = ps.executeUpdate();
				data.put("result", (rowsAffected > 0));

				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(data);

			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
