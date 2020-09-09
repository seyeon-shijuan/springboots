package kr.goodee.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.goodee.exception.ShopException;
import kr.goodee.logic.Item;
import kr.goodee.logic.ShopService;

@Controller  // @Component + controller(요청을 받을 수 있는 객체)
@RequestMapping("item") // /item/ 요청시
public class ItemController {

	@Autowired
	private ShopService service;
	
	@RequestMapping("list") // /item/list.shop
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		List<Item> itemList = service.getItemList();
		mav.addObject("itemList",itemList);
		return mav;		
	}
	@RequestMapping("create") // /item/create.shop
	public String addform(Model model) {
		model.addAttribute(new Item());
		return "item/add";		
	}
	@RequestMapping("register") 
	public ModelAndView add(@Valid Item item, BindingResult bresult,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("item/add");
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		service.itemCreate(item,request);
		mav.setViewName("redirect:/item/list.shop");
		return mav;		
	}
	@PostMapping("update")
	public ModelAndView update	(@Valid Item item, 
			BindingResult bresult,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("item/edit");
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		//db,파일업로드
		service.itemUpdate(item,request);
		mav.setViewName("redirect:/item/detail.shop?id="+item.getId());
		return mav;
	}
	@GetMapping("delete")
	public ModelAndView itemDelete(String id) {
		ModelAndView mav = new ModelAndView();
		service.itemDelete(id);
		mav.setViewName("redirect:/item/list.shop");
		return mav;		
	}
	
	@GetMapping("*") // /item/*.shop 
	public ModelAndView detail(Integer id) {
		ModelAndView mav = new ModelAndView();
		try {
		  Item item = service.getItem(id);
		  mav.addObject("item",item);
		} catch(IndexOutOfBoundsException e) {
			throw new ShopException
			("없는 상품을 조회 했습니다. 상품 id를 확인 하세요","list.shop");
		}
		return mav;
	}
}
