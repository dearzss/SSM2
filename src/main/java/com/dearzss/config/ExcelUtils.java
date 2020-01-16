package com.dearzss.config;

import com.dearzss.domain.Files;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExcelUtils {

    private final static String excel2003L = ".xls";    //2003- 版本的excel
    private final static String excel2007U = ".xlsx";  //2007+ 版本的excel

    public static List<String[][]> createArray(InputStream in, String fileName) throws Exception{
        List<String[][]> lists= new ArrayList<String[][]>();

        //创建Excel工作薄
        Workbook work = getWorkbook(in,fileName);
        if(null == work){
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;  //页数

        int sheetNum = work.getNumberOfSheets();
        //遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            int totalRows = sheet.getLastRowNum();
            int RowCells=sheet.getRow(0).getLastCellNum();
            String[][] array = new String[totalRows + 1][RowCells];
            lists.add(array);
        }
        return lists;
    }

    public static List<String[][]> getBankListByExcel(InputStream in, String fileName, List<String[][]> lists) throws Exception{

        //创建Excel工作薄
        Workbook work = getWorkbook(in,fileName);
        if(null == work){
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;  //页数
        Row row = null;  //行数
        Cell cell = null;  //列数
        //遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);

            String[][] strings = lists.get(i);

            if(sheet==null || sheet.getSheetName().toLowerCase().indexOf("not required") != -1){continue;}

            //遍历当前sheet中的所有行
            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if(row==null){continue;}

                //遍历所有的列
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    strings[j][y] = getValue(cell);
                }
            }
        }

        return lists;

    }

    public static Workbook getWorkbook(InputStream inStr,String fileName) throws Exception{
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if(excel2003L.equals(fileType)){
            wb = new HSSFWorkbook(inStr);  //2003-
        }else if(excel2007U.equals(fileType)){
            wb = new XSSFWorkbook(inStr);  //2007+
        }else{
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }

    public static String getValue(Cell cell) throws Exception{
        String value = "";
        if(null==cell){
            return value;
        }
        switch (cell.getCellType()) {
            //数值型
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    //如果是date类型则 ，获取该cell的date值
                    Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                    value = format.format(date);
                }else {// 纯数字
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().indexOf(".")>0){
                        value = cell.getStringCellValue().toString();
                    }else {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        BigDecimal big = new BigDecimal(cell.getNumericCellValue());
                        value = big.toString();
                        //解决1234.0  去掉后面的.0
                        if (null != value && !"".equals(value.trim())) {
                            String[] item = value.split("[.]");
                            if (1 < item.length && "0".equals(item[1])) {
                                value = item[0];
                            }
                        }
                    }
                }
                break;
            //字符串类型
            case Cell.CELL_TYPE_STRING:
                value = cell.getStringCellValue().toString();
                break;
            // 公式类型
            case Cell.CELL_TYPE_FORMULA:
                //读公式计算值
                value = String.valueOf(cell.getNumericCellValue());
                if (value.equals("NaN")) {// 如果获取的数据值为非法值,则转换为获取字符串
                    value = cell.getStringCellValue().toString();
                }
                break;
            // 布尔类型
            case Cell.CELL_TYPE_BOOLEAN:
                value = " "+ cell.getBooleanCellValue();
                break;
            default:
                value = cell.getStringCellValue().toString();
        }
        if("null".endsWith(value.trim())){
            value="";
        }
        return value;
    }

    public static List<Files> getAllFileName(String path){
        List<Files> filesList = new ArrayList<>();
        File file = new File(path);
        File[] files = file.listFiles();
        if(files != null){
            for(File file1 : files) {
                if(file1.getName().indexOf(excel2003L) != -1 || file1.getName().indexOf(excel2007U) != -1) {
                    Files files1 = new Files();
                    files1.setFileName(file1.getName());

                    Calendar cal = Calendar.getInstance();
                    long time = file1.lastModified();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    cal.setTimeInMillis(time);
                    files1.setModifyDate(formatter.format(cal.getTime()));
                    filesList.add(files1);
                }
            }
        }
        return filesList;
    }
}
