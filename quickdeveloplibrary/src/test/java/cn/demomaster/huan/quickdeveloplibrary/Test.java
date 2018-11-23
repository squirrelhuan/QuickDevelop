package cn.demomaster.huan.quickdeveloplibrary;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.view.tabmenu.TabMenuLayout;

/**
 * @author squirrel桓
 * @date 2018/11/23.
 * description：
 */
public class Test {


    private List<Character> list;
    private List<String> aaa;
    @org.junit.Test
    public  void test(){
        testa(Test.class,"menuTabs");
    }
    public  void testa(Class clazza,String fieldName){
        // TODO Auto-generated method stub
        try {
            Field listField = clazza.getDeclaredField(fieldName);
            System.out.println(listField.getGenericType());
            //获取 list 字段的泛型参数
            ParameterizedType listGenericType = (ParameterizedType) listField.getGenericType();
            Type[] listActualTypeArguments = listGenericType.getActualTypeArguments();
            System.out.println(listActualTypeArguments[listActualTypeArguments.length - 1]);
            for (int i = 0; i < listActualTypeArguments.length; i++) {
                System.out.println(listActualTypeArguments[i]);
                testa(Class.forName(listActualTypeArguments[i].toString()), "showTextField");
            }
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

   /* List<TabMenuLayout.MenuTab> menuTabs;
    @org.junit.Test
    public void aaa(){
        menuTabs = new ArrayList<>();

        List<TabMenuLayout.TextModel> textModels = new ArrayList<>();
        textModels.add(new TabMenuLayout.TextModel("a",1,"11a"));
        TabMenuLayout.MenuTab menuTab1 = new TabMenuLayout.MenuTab("name", "code", textModels);
        menuTabs.add(menuTab1);

        List<TabMenuLayout.TextModel> textModels2 = new ArrayList<>();
        textModels2.add(new TabMenuLayout.TextModel("b",2,"11b"));
        TabMenuLayout.MenuTab menuTab2 = new TabMenuLayout.MenuTab("name", "code", textModels2);
        menuTabs.add(menuTab2);

        List<TabMenuLayout.TextModel> textModels3 = new ArrayList<>();
        textModels3.add(new TabMenuLayout.TextModel("e",7,"eeb"));
        textModels3.add(new TabMenuLayout.TextModel("f",8,"f8"));
        TabMenuLayout.MenuTab menuTab3 = new TabMenuLayout.MenuTab("name", "code", textModels3);
        menuTabs.add(menuTab3);

        for(TabMenuLayout.MenuTab menuTab:menuTabs){
            List list = menuTab.getRelList();
            Class clazz = menuTab.getClazz();
            clazz = list.get(0).getClass();
            System.out.println("C="+clazz);
            testa(clazz,menuTab.getShowTextField());
            //System.out.println("C="+menuTab.getRelList().getClass());
        }
        //test();

        //addMenus(menuTabs);
    }*/

}
