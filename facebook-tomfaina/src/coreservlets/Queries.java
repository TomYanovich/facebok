package coreservlets;

public class Queries {

	public static String getAllUsers = "(SELECT  CONCAT(u.firstName, ' ', u.lastName) as 'FullName',u.username, 'Friends' as 'Category' "
			+ " from facebookdb.tbluser as u INNER JOIN tblfriend AS f ON u.username = f.secondUser "
			+ " where  u.firstName  like ? and f.firstUser = ?) Union "
			+ " select CONCAT(u.firstName, ' ', u.lastName) as 'FullName',u.username, 'Others' as 'Category' "
			+ " from  facebookdb.tbluser as u where u.username like ? and (u.username != ?) "
			+ " and u.username Not in (select f.secondUser from tblfriend as f where f.firstUser = ?) ";

	public static String addFriend = "INSERT INTO tblfriend  VALUES (?,?)";

	public static String getOnLineFriends = "(SELECT  CONCAT(u.firstName, ' ', u.lastName) as 'FullName',u.username,u.isOnline "
			+ " from facebookdb.tbluser as u INNER JOIN tblfriend AS f ON u.username = f.secondUser "
			+ " where  f.firstUser = ?)";

	public static String getPosts = "SELECT CONCAT(u.firstName, ' ', u.lastName) as 'FullName' ,u.profilePic "
			+ " ,p.postId , DATE_FORMAT(p.date , '%d/%m/%y') as date, p.content, p.author FROM "
			+ " facebookdb.tblpost as p inner join tbluser as u"
			+ " on p.author = u.username where p.author in (SELECT seconduser FROM facebookdb.tblfriend"
			+ " where firstuser = ?) || p.author=? order by date desc";

	public static String getProfile = "SELECT *\n" + "FROM tbluser \n" + "WHERE username = ?";

	public static String getMessages = "SELECT content,  firstName, lastName\n"
			+ "FROM tblmessage inner join tbluser on fromUser=username\n" + "where toUser=?";

	public static String getNotifications = "SELECT firstName, lastName, content\n"
			+ "FROM tblnotification inner join tbluser on username=userOwner\n" + "where userOwner=?";

	public static String getComments = "SELECT firstName,lastName,content, profilePic "
			+ " FROM facebookdb.tblcomment inner join tbluser on author=username" + " where postId=?";

	public static String postComment = "INSERT INTO `facebookdb`.`tblcomment`\n"
			+ "(`postId`,`content`,`author`) VALUES (?,?,?)";
	
	public static String GetUserDetails = "SELECT * FROM tbluser WHERE username = ? AND password = ?";

	public static String getNewPosts = "SELECT CONCAT(u.firstName, ' ', u.lastName) as 'FullName' ,u.profilePic "
			+ " ,p.postId , DATE_FORMAT(p.date , '%d/%m/%y') as date, p.content, p.author FROM "
			+ " facebookdb.tblpost as p inner join tbluser as u "
			+ " on p.author = u.username "
			+ " where (p.author in (SELECT seconduser FROM facebookdb.tblfriend where firstuser = ?) || p.author= ? ) "
			+ " and FIND_IN_SET(CONVERT(p.postId,CHAR),?) = 0  order by date desc;"; // p.author=?
	
	/**
	 * get all friends of the user and the other users
	 */
	public static String getFriends = "SELECT * FROM facebookdb.tbluser WHERE username <> ?";
	/**
	 * get friends of the user we are viewing that are not friends of the
	 * connected user
	 */
	public static String getNotFirends = "(SELECT  CONCAT(u.firstName, ' ', u.lastName) as 'FullName',u.username, isOnline "
			+ " , CASE WHEN u.username in (select secondUser from tblfriend where secondUser = u.username and firstUser = ?)  THEN 'Yes' "
			+ " ELSE 'No' END AS 'isFriend' "
			+ "	from facebookdb.tbluser as u INNER JOIN tblfriend AS f ON u.username = f.secondUser where  f.firstUser = ?)";

	public static String addUser = "INSERT  into tbluser  values(?,?,?,?,?,?,?,?)";

	public static String isFriend = "SELECT CASE WHEN EXISTS (SELECT* FROM facebookdb.tblfriend WHERE firstuser = ? and secondUser = ?)"
			+ "THEN 'true' ELSE 'false' END as 'isFriend'";
	
	public static String PostPost = "INSERT into tblpost (date, content, author) values(?,?,?)";
	
	public static String getLastPost = "SELECT * FROM tblPost ORDER BY postId DESC LIMIT 1";
	
//	public static String getPicture = "SELECT profilePic FROM tblUser WHERE username = 'snufkin'";
}
