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

import org.json.simple.JSONObject;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String userid;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Register() {
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

		try {
			HttpSession session = request.getSession();
			coreservlets.MyConnection con = new coreservlets.MyConnection();

			if (session.getAttribute("connection") != null) {
				con = (coreservlets.MyConnection) session.getAttribute("connection");
			} else {
				con = new coreservlets.MyConnection();
				session.setAttribute("connection", con);
			}

			if (con == null) {
				return;
			}

			userid = request.getParameter("un");
			if (userid != null) {
				// login
				login(request, response, con);
			} else {
				// register
				register(request, response, con);
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void register(HttpServletRequest request, HttpServletResponse response, coreservlets.MyConnection con)
			throws SQLException, IOException {
		userid = request.getParameter("userRegister");
		System.out.println("userid: " + userid);
		PreparedStatement ps;
		ps = con.getConnection().prepareStatement(Queries.addUser);

		ps.setString(1, userid);
		ps.setString(2, request.getParameter("fn"));
		ps.setString(3, request.getParameter("ln"));
		ps.setString(4, request.getParameter("password"));
		ps.setString(5, request.getParameter("email"));
		ps.setString(6, "Pics/noPic.png"); //TODO: change
		ps.setString(7, "Pics/noCover.png");
		ps.setInt(8, 1);
		int rowsAffected = ps.executeUpdate();
		ps.close();

		if (rowsAffected > 0) {
			request.getSession().setAttribute("userid", userid);
			response.sendRedirect("Main.jsp");
		} else{
			response.sendRedirect("LogReg.jsp?err=RegisterError");
		}
	}
	
	private void login(HttpServletRequest request, HttpServletResponse response, coreservlets.MyConnection con)
			throws SQLException, IOException {
		
		String un = request.getParameter("un");
		String pw = request.getParameter("pw");

		PreparedStatement ps = null;
		ResultSet rs;
	
		ps = con.getConnection().prepareStatement(Queries.GetUserDetails);
		ps.setString(1, un);
		ps.setString(2, pw);
		rs = ps.executeQuery();
		JSONObject userDetails = new JSONObject();
		boolean isExist = false;
		
		while (rs.next()){
			isExist = true;
			userDetails.put("username", rs.getString("username"));
			userDetails.put("firstName", rs.getString("firstName"));
			userDetails.put("lastName",  rs.getString("lastName"));
			userDetails.put("profilePic", rs.getString("profilePic"));
			userDetails.put("coverPic", rs.getString("coverPic"));
		}
		
		if (isExist) {
			request.getSession().setAttribute("userid", userid);
			request.getSession().setAttribute("userDetails", userDetails);
			response.sendRedirect("Main.jsp");
		} else {
			response.sendRedirect("LogReg.jsp?err=userDoesNotExist");
		}
	}
}
