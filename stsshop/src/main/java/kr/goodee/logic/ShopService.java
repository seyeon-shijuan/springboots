package kr.goodee.logic;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import kr.goodee.dao.BoardDao;
import kr.goodee.dao.ItemDao;
import kr.goodee.dao.SaleDao;
import kr.goodee.dao.SaleItemDao;
import kr.goodee.dao.UserDao;

@Service //@Component + service(Controller와 dao 중간 역할)
public class ShopService {
	@Autowired
	private ItemDao itemDao;
	@Autowired
	private UserDao userDao;
    @Autowired
    private SaleDao saleDao;
    @Autowired
    private SaleItemDao saleItemDao;
    @Autowired
    private BoardDao boardDao;
    
	public List<Item> getItemList() {
		return itemDao.list();
	}
	public Item getItem(Integer id) {
		return itemDao.selectOne(id);
	}
	//파일 업로드, dao에 데이터 전달
	public void itemCreate(Item item, HttpServletRequest request) {
		//업로드 되는 파일이 존재.
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			uploadFileCreate(item.getPicture(),request,"item/img/");
			item.setPictureUrl
			           (item.getPicture().getOriginalFilename());
		}
		itemDao.insert(item);
	}
	private void uploadFileCreate(MultipartFile picture, 
			HttpServletRequest request, String path) {
		//picture : 파일의 내용 저장
		String orgFile = picture.getOriginalFilename();

		String uploadPath=request.getServletContext().getRealPath("/")
				+ path;
		File fpath = new File(uploadPath);
		if(!fpath.exists()) fpath.mkdirs();
		try {
			//파일의 내용을 실제 파일로 저장
			picture.transferTo(new File(uploadPath + orgFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void itemUpdate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			uploadFileCreate(item.getPicture(),request,"item/img/");
			item.setPictureUrl
			           (item.getPicture().getOriginalFilename());
		}
		itemDao.update(item);
	}
	public void itemDelete(String id) {
		itemDao.delete(id);		
	}
	public void userInsert(User user) {
		userDao.insert(user);
	}
	public User getUser(String userid) {
		return userDao.selectOne(userid);
	}
/*
 * db에  sale 정보와 saleitem 정보 저장. 저장된 내용을 Sale 객체로 리턴
 * 1. sale 테이블의 saleid 값을 설정 => 최대값 + 1
 * 2. sale의 내용 설정. => insert
 * 3. Cart 정보로부터 SaleItem 내용 설정 	=> insert
 * 4. 모든 정보를 Sale 객체로 저장
 */
	public Sale checkend(User loginUser, Cart cart) {
		Sale sale = new Sale();
		int maxno = saleDao.getMaxSaleid();
		sale.setSaleid(++maxno);
		sale.setUser(loginUser);
		sale.setUserid(loginUser.getUserid());
		saleDao.insert(sale);
		//장바구에서 판매 상품 정보 
		List<ItemSet> itemList = cart.getItemSetList(); //Cart 상품정보
		int i=0;
		for(ItemSet is : itemList) {
			int seq = ++i;
			SaleItem saleItem = 
					new SaleItem(sale.getSaleid(),seq,is);
			sale.getItemList().add(saleItem); //Sale 객체의 SaleItem추가
			saleItemDao.insert(saleItem);
		}
		return sale;
	}
	public List<Sale> salelist(String id) {
		return saleDao.list(id); //사용자 id
	}
	public List<SaleItem> saleItemList(int saleid) {
		return saleItemDao.list(saleid); //saleid 주문번호
	}
	public void userUpdate(User user) {
		userDao.update(user);
	}
	public void userDelete(String userid) {
		userDao.delete(userid);
	}
	public List<User> userList() {
		return userDao.list();
	}
	public List<User> userList(String[] idchks) {
		return userDao.list(idchks);
	}
	public void boardWrite(Board board, HttpServletRequest request) {
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
		  uploadFileCreate(board.getFile1(), request, "board/file/");
		  board.setFileurl(board.getFile1().getOriginalFilename());
		}
		int max = boardDao.maxnum();
		board.setNum(++max);
		board.setGrp(max);
		boardDao.insert(board);
	}
	public int boardcount(String searchtype,String searchcontent) {
		return boardDao.count(searchtype,searchcontent);
	}
	public List<Board> boardlist(Integer pageNum, int limit,
			String searchtype,String searchcontent) {
		return boardDao.list(pageNum,limit,searchtype,searchcontent);
	}
	public Board getBoard(Integer num, boolean able) {
		if(able) {
			boardDao.readcntadd(num);
		}		
		return boardDao.selectOne(num);
	}
	public void boardReply(Board board) {
		boardDao.updateGrpStep(board); //기존 답글의 grpstep 증가.
		int max = boardDao.maxnum();
		//답글 정보 수정
		board.setNum(++max);
		board.setGrplevel(board.getGrplevel() + 1);
		board.setGrpstep(board.getGrpstep() + 1);
		boardDao.insert(board);
	}	
	public void boardUpdate
    (Board board, HttpServletRequest request) {
		//업로드 되는 파일이 존재하는 경우.
		if(board.getFile1()!=null && !board.getFile1().isEmpty()){
			uploadFileCreate(board.getFile1(),
	             request, "board/file/");
			board.setFileurl(board.getFile1().getOriginalFilename());
		}
		boardDao.update(board);
	}
	public void boardDelete(Board board) {
		boardDao.delete(board.getNum());
	}
	public Map<String, Object> graph1() {
		Map<String,Object> map = new HashMap<String,Object>();
		for(Map<String,Object> m : boardDao.graph1()) {
			map.put((String)m.get("name"), m.get("cnt"));
		}
		return map;
	}		
	public Map<String, Object> graph2() {
		Map<String,Object> map = new HashMap<String,Object>();
		for(Map<String,Object> m : boardDao.graph2()) {
			map.put((String)m.get("regdate"), m.get("cnt"));
		}
		return map;
	}	
}
