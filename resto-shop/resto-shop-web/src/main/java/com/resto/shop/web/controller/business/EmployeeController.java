 package com.resto.shop.web.controller.business;

 import com.google.zxing.WriterException;
 import com.resto.brand.core.entity.Result;
 import com.resto.brand.core.util.FileToZip;
 import com.resto.brand.core.util.QRCodeUtil;
 import com.resto.brand.web.model.ShopDetail;
 import com.resto.brand.web.service.ShopDetailService;
 import com.resto.shop.web.constant.ERoleDto;
 import com.resto.shop.web.controller.GenericController;
 import com.resto.shop.web.model.ERole;
 import com.resto.shop.web.model.Employee;
 import com.resto.shop.web.model.EmployeeRole;
 import com.resto.shop.web.service.ERoleService;
 import com.resto.shop.web.service.EmployeeService;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RequestParam;
 import org.springframework.web.bind.annotation.ResponseBody;
 import org.springframework.web.servlet.ModelAndView;

 import javax.annotation.Resource;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import javax.validation.Valid;
 import java.io.*;
 import java.util.*;

 import static com.resto.shop.web.controller.business.QrCodeController.deleteFile;

 @Controller
 @RequestMapping("employee")
 class EmployeeController extends GenericController{

     @Resource
     private EmployeeService employeeService;

	 @Resource
	 private  ShopDetailService shopDetailService;

	 @Resource
	 private ERoleService eRoleService;

     @Resource
   private com.resto.brand.web.service.EmployeeService employeeBrandService;
	
	@RequestMapping("/list")
    public void list(){
    }


	@RequestMapping("/list_all")
	@ResponseBody
	public List<Employee> listData(){
		return employeeService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		Employee employee = employeeService.selectById(id);
		return getSuccessResult(employee);
	}

	@RequestMapping("listOne")
	@ResponseBody
	public  Result listOne(Long employeeId){
			Employee employee = employeeService.selectOneById(employeeId);
			return getSuccessResult(employee);
	}

	 @RequestMapping("listIds")
	 @ResponseBody
	 public  Result listIds(Long employeeId){
		 Employee employee = employeeService.selectOneById(employeeId);
		 Set<String> ids = new HashSet<>();
		 if(employee!=null){
				if(!employee.getEmployeeRoleList().isEmpty()){
						for(EmployeeRole er : employee.getEmployeeRoleList()){
								String id = er.getShopId()+"_"+er.geteRole().getId();
								String shopId = er.getShopId();
								ids.add(id);
								ids.add(shopId);
						}
				}
		 }
		 return getSuccessResult(ids);
	 }


	
//	@RequestMapping("addData")
//	@ResponseBody
//	public Result create(@Valid Employee employee){
//		return employeeService.insertOne(employee,getCurrentBrandUser(),getCurrentBrandId());
//	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid Employee employee){
		employeeService.update(employee);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
	    //做假删除

		return employeeService.updateEmployee(id);
	}


	@RequestMapping("add")
	public String add(){
		return "employee/save";
	}



	 @RequestMapping("employee_role")
	 public ModelAndView assignPermissions(Long employeeId){

	     //查询出该员工所有店铺的所有角色
        Employee employee =  employeeService.selectOneById(employeeId);

		 //查询出所有的店铺角色
		 List<ShopDetail> shops = shopDetailService.selectByBrandId(getCurrentBrandId());
		 //查询出所有的定义的角色
		 List<ERole> eRoles = eRoleService.selectList();
		 //定义一个map封装店铺和 角色的数据
		 List<ERoleDto> elist = new ArrayList<>();
		 for (ShopDetail shop : shops) {
			 ERoleDto eDto = new ERoleDto();
			 eDto.setShopId(shop.getId());
			 eDto.setShopName(shop.getName());
			 eDto.seteRolelist(eRoles);
			 elist.add(eDto);
		 }

		 ModelAndView mv = new ModelAndView("employee/employee_role");
		 mv.addObject("employee", employee);
         mv.addObject("employeeId",employeeId);
		 mv.addObject("elist",elist);

         return mv;
	 }

//	 @RequestMapping("assign_form")
//	 @ResponseBody
//	 public Result assignForm(String employeeId,String id) {
//         //31164cebcc4b422685e8d9a32db12ab8_1002,31164cebcc4b422685e8d9a32db12ab8_1003
//
//        employeeService.updateSelected(Long.parseLong(employeeId),id,getCurrentBrandUser());
//		 return new Result(true);
//	 }


	 @RequestMapping(" listAllShopsAndRoles")
	 @ResponseBody
	 public Result listAllShopsAndRoles() {
		//查询所有的店铺
		 List<ShopDetail> shops = shopDetailService.selectByBrandId(getCurrentBrandId());
		//查询出所有的定义的角色
		 List<ERole> eRoles = eRoleService.selectList();
		 //定义一个map封装店铺和 角色的数据
		List<ERoleDto> elist = new ArrayList<>();
		 for (ShopDetail shop : shops) {
			 ERoleDto eDto = new ERoleDto();
			 eDto.setShopId(shop.getId());
			 eDto.setShopName(shop.getName());
			 eDto.seteRolelist(eRoles);
			 elist.add(eDto);
		 }
		 return getSuccessResult(elist);
	 }


	 @RequestMapping("QR")
     @ResponseBody
     public  Result createQR(String employeeId, HttpServletResponse httpResponse) throws WriterException {
         OutputStream out = null;
         try {
             out = httpResponse.getOutputStream();
             QRCodeUtil.createQRCode(String.valueOf(employeeId),"png",out);

         } catch (IOException e) {
             e.printStackTrace();
         }finally {
             if(out==null){
                 try {
                     out.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }

         }
         return  getSuccessResult();

     }


     @RequestMapping("checkeTelephone")
     @ResponseBody
     public  Result checkeTelephone(String telephone){

        return employeeService.checkeTelephone(telephone);

     }


     public Result run(String zipname,List<Employee>shopElist ,String id,HttpServletRequest request)
             throws IOException, InterruptedException, WriterException {
         String fileSavePath = getFilePath(request,null);
         deleteFile(new File(fileSavePath));//删除历史生成的文件
         String filepath;//生成二位吗的文件路径
         String fileName;//二维码名字

         //查询所有id
         List<com.resto.brand.web.model.Employee> list = employeeBrandService.selectList();

         if(null==id||"".equals(id)){  //id没好值说明是点全部下载
             //生成的zip的路径
             filepath = getFilePath(request,zipname);
             for(Employee e:shopElist){
                 for(com.resto.brand.web.model.Employee employee2:list){
                     if(e.getTelephone().equals(employee2.getTelephone())){
                         e.setBrandEmployeeId(employee2.getId());
                     }
                 }
                 fileName = e.getName()+".jpg";
                 QRCodeUtil.createQRCode(e.getBrandEmployeeId(),filepath,fileName);
             }
         }else {//如果是生成个人的下载
             //生成的zip路径
             filepath = getFilePath(request,zipname);
             //生成二位码的名字也是
             fileName =zipname+".jpg";
             QRCodeUtil.createQRCode(id,filepath,fileName);
         }
             //打包
             FileToZip.fileToZip(filepath, fileSavePath, zipname);
             Result result = new Result(true);
             result.setMessage(zipname+".zip");
             System.gc();//手动回收垃圾，清空文件占用情况，解决无法删除文件
             return result;

     }


     @RequestMapping("/downloadFile")
     public String donloadFile(@RequestParam(value = "id" ,required = false,defaultValue = "") String id ,@RequestParam(value = "name",required = false,defaultValue = "") String name, HttpServletRequest request,
                               HttpServletResponse response) throws IOException, WriterException, InterruptedException {
         String zipname;//定义zip的名字

         String shopName = shopDetailService.selectById(getCurrentShopId()).getName();
         List<Employee> employeeList = employeeService.selectList();
         //如果当前值为空说明是生成全部的文件
         if("".equals(id)||null==id){
             zipname  = shopName;
         }else{
             zipname = name;
         }

         //第一步生成zip文件
         Result r = run(zipname,employeeList,id,request);
         String fileName = r.getMessage();
         response.setContentType("text/html;charset=utf-8");
         request.setCharacterEncoding("UTF-8");
         java.io.BufferedInputStream bis = null;
         java.io.BufferedOutputStream bos = null;
         String downLoadPath = getFilePath(request,fileName);
         try {
             long fileLength = new File(downLoadPath).length();
             response.setContentType("application/x-msdownload;");
             response.setHeader("Content-disposition", "attachment; filename="
                     + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
             response.setHeader("Content-Length", String.valueOf(fileLength));
             bis = new BufferedInputStream(new FileInputStream(downLoadPath));
             bos = new BufferedOutputStream(response.getOutputStream());
             byte[] buff = new byte[2048];
             int bytesRead;
             while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                 bos.write(buff, 0, bytesRead);
             }
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             if (bis != null){
                 bis.close();
             }
             if (bos != null){
                 bos.close();
             }
         }
         return null;
     }

     public String getFilePath(HttpServletRequest request,String fileName){
         String systemPath = request.getServletContext().getRealPath("");
         systemPath = systemPath.replaceAll("\\\\", "/");
         int lastR = systemPath.lastIndexOf("/");
         systemPath = systemPath.substring(0,lastR)+"/";
         String filePath = "qrCodeFiles/";
         if(fileName!=null){
             filePath += fileName;
         }
         return systemPath+filePath;
     }



}
