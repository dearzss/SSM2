package com.dearzss.controller;

import com.dearzss.config.Utils;
import com.dearzss.config.ExcelUtils;
import com.dearzss.domain.Files;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/excel")
public class ExcelController {

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void uploadExcel(HttpSession session, HttpServletRequest request, HttpServletResponse response,MultipartFile[] attachs) throws IOException {
        //定义两个上传文件的路径
        String errorinfo = null;

        //定义上传过程管理标记
        boolean flag = true;
        //定义文件保存的位置
        String path = request.getSession().getServletContext().getRealPath("statics"+ File.separator+"uploadfiles");
        for(int i=0;i<attachs.length;i++){
            MultipartFile attach = attachs[i];
            //判断文件是否为空
            if(!attach.isEmpty()) {
                //获取源文件名
                String fileName = attach.getOriginalFilename();
                //获取源文件名后缀
                String prefixName = FilenameUtils.getExtension(fileName);

                File targetFile = new File(path, fileName);
                if (!targetFile.exists()) {
                    targetFile.mkdirs();
                }
                System.out.println("targetFile : " + targetFile);
                //将上传的文件保存
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    session.setAttribute(errorinfo, "上传失败！");
                    flag = false;
                }
            }
        }
        response.sendRedirect(request.getContextPath() + "/excel/findAllExcel");
    }

    @RequestMapping("findAllExcel")
    public ModelAndView listAllExcel(ModelAndView mv,HttpServletRequest request) throws IOException {
        String path = request.getSession().getServletContext().getRealPath("statics"+ File.separator+"uploadfiles");
        System.out.println("path : " + path);
        List<Files> fileNameList = ExcelUtils.getAllFileName(path);
        mv.addObject("fileNameList",fileNameList);
        mv.setViewName("listAllExcel");
        return mv;
    }

    @RequestMapping("generateScripts")
    public void generateScripts(@RequestParam("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception{
        String path = request.getSession().getServletContext().getRealPath("statics"+ File.separator+"uploadfiles");
        File file = new File(path + File.separator+fileName);
        InputStream in = new FileInputStream(file);
        InputStream inputStream = new FileInputStream(file);

        List<String[][]> arrayList  = ExcelUtils.createArray(in,fileName);
        List<String[][]> lists = ExcelUtils.getBankListByExcel(inputStream, fileName, arrayList);
        List<HashMap> hashMapList = new ArrayList<>();
        List<HashMap> hashMapListResult = new ArrayList<>();
        for(int j=0; j<lists.size(); j++){
            String[] tables = null;
            String[][] strings = lists.get(j);
            HashMap map = new HashMap<>();
            if(strings.length <= 2) continue;
            for(int x=0; x<strings.length; x++){
                if(x == 0) continue;
                if(x == 1){
                    for(int y=0; y<strings[x].length; y++) {
                        if (x == 1 && y == 0) {
                            tables = strings[x][y].split(";");
                            continue;
                        }
                        if (tables == null) continue;
                        for (String tablename : tables) {
                            if (y != 0 && strings[1][y] != null && strings[1][y] != "null" && strings[1][y].indexOf(tablename) != -1) {
                                if(strings[1][y].indexOf(";",strings[1][y].indexOf(tablename)) != -1){
                                    map.put(j + ":" + tablename + ":" + y, strings[1][y].substring(strings[1][y].indexOf(tablename) + tablename.length() + 1,strings[1][y].indexOf(";",strings[1][y].indexOf(tablename))));
                                }else {
                                    map.put(j + ":" + tablename + ":" + y, strings[1][y].substring(strings[1][y].indexOf(tablename) + tablename.length() + 1));
                                }
                            }
                        }
                    }
                }
            }
            hashMapList.add(map);
        }

        for(HashMap map : hashMapList){
            Set set=map.entrySet();
            Iterator it=set.iterator();
            int sheetNum = 0;
            if(it.hasNext()){
                Map.Entry me=(Map.Entry)it.next();
                sheetNum = Integer.parseInt(me.getKey().toString().split(":")[0]);
            }

            for (String tablename : lists.get(sheetNum)[1][0].split(";")) {
                it = set.iterator();
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("INSERT INTO " + tablename + "(");

                HashMap hashMap = new HashMap();
                StringBuffer sb = new StringBuffer();
                while (it.hasNext()) {
                    Map.Entry me = (Map.Entry) it.next();

                    if (me.getKey().toString().split(":")[1].equalsIgnoreCase(tablename)) {
                        stringBuffer.append(me.getValue() + ",");
                        sb.append(me.getKey().toString().split(":")[2] + ",");

                    }
                }
                hashMap.put(sheetNum + ":" +stringBuffer.toString(), sb.toString());
                hashMapListResult.add(hashMap);
            }
        }

        File file1 = new File(path + File.separator + fileName.split("\\.")[0] + ".txt");
        if(!file1.exists()){
            file1.createNewFile();
        }else{
            file1.delete();
            file1.createNewFile();
        }

        FileWriter fileWritter = new FileWriter(path + File.separator + fileName.split("\\.")[0] + ".txt",true);
        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);

        for(HashMap map : hashMapListResult){
            Set set=map.entrySet();
            Iterator it=set.iterator();
            int sheetNum = 0;
            StringBuffer stringBuffer = new StringBuffer();
            String record = "";

            while (it.hasNext()){
                Map.Entry me=(Map.Entry)it.next();
                sheetNum = Integer.parseInt(me.getKey().toString().split(":")[0]);
                stringBuffer.append(me.getKey().toString().split(":")[1].substring(0,me.getKey().toString().split(":")[1].length() -1) + ") VALUES (");
                for(int x=2; x<lists.get(sheetNum).length; x++){
                    String cellNum = me.getValue().toString().substring(0,me.getValue().toString().length() - 1);
                    for(String cellValue : cellNum.split(",")) {
                        if(Utils.isValidDate(lists.get(sheetNum)[x][Integer.parseInt(cellValue)])){
                            stringBuffer.append("CONVERT(datetime,'" + lists.get(sheetNum)[x][Integer.parseInt(cellValue)] + "',120),");
                        }else if(Utils.isNumeric(lists.get(sheetNum)[x][Integer.parseInt(cellValue)])){
                            stringBuffer.append(lists.get(sheetNum)[x][Integer.parseInt(cellValue)] + ",");
                        }else {
                            stringBuffer.append("'" + lists.get(sheetNum)[x][Integer.parseInt(cellValue)] + "',");
                        }
                    }
                    stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                    stringBuffer.append("),(");
                }
                stringBuffer.delete(stringBuffer.length() - 2,stringBuffer.length());
                stringBuffer.append(";");
            }
            bufferWritter.flush();
            record = stringBuffer.toString().replaceAll("'null'","null");
            bufferWritter.write(record);
            bufferWritter.newLine();
            bufferWritter.write("-------------------------------------------------------------");
            bufferWritter.newLine();
        }

        bufferWritter.close();
        fileWritter.close();
        System.out.println("finish");

        response.sendRedirect(request.getContextPath() + "/excel/findAllExcel");
    }

    @RequestMapping("downloadScripts")
    public void downloadScripts(@RequestParam("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = request.getSession().getServletContext().getRealPath("statics" + File.separator + "uploadfiles" + File.separator + fileName.split("\\.")[0] + ".txt");
        File file = new File(path);

        if(!file.exists()){
            response.setContentType("text/html; charset=UTF-8");//注意text/html，和application/html
            response.getWriter().print("<html><body><script type='text/javascript'>alert('File not exists!!!');</script></body></html>");
            response.getWriter().close();
            System.out.println("File not exists!!!");

            response.sendRedirect(request.getContextPath() + "/excel/findAllExcel");
        }

        //转码，免得文件名中文乱码
        //设置文件下载头
        response.setHeader("content-disposition", "attachment;filename="+URLEncoder.encode(file.getName(), "UTF-8"));
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        // 读取要下载的文件，保存到文件输入流
        FileInputStream in = new FileInputStream(path);
        // 创建输出流
        OutputStream out = response.getOutputStream();
        // 创建缓冲区
        byte buffer[] = new byte[1024]; // 缓冲区的大小设置是个迷  我也没搞明白
        int len = 0;
        //循环将输入流中的内容读取到缓冲区当中
        while((len = in.read(buffer)) > 0){
            out.write(buffer, 0, len);
        }
        //关闭文件输入流
        in.close();
        // 关闭输出流
        out.close();

        return;
    }
}
