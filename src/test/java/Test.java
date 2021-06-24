public class Test {
    public static void main(String[] args) {
        
        String recipe = "air|air|air";
        for(String m : recipe.split("\\|")){
            System.out.println(m);
        }
    }
}
