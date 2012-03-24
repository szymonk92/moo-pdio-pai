package sys;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ClassUtils;


public class ModulLoader<T> {
	
public ModulLoader(){}

public static boolean isInterface(Class<?> modul, String interfejs) {
        Class<?> iterfaceProcessor = null;
        try {
            iterfaceProcessor = Class.forName(interfejs);
        } catch (ClassNotFoundException e) {
            Messages.error("Nie znaleziono interfejsu:" + e.getMessage());
        }
        if (iterfaceProcessor != null) {
            List<Class<?>> interfejsy = ClassUtils.getAllInterfaces(modul);
            for (Class<?> c : interfejsy) {
                if (c == iterfaceProcessor) {
                    return true;
                }
            }
        }
        return false;
    }
public static boolean isClass(Class<?> modul, String exdendetClass) {
        Class<?> classProcessor = null;
        try {
            classProcessor = Class.forName(exdendetClass);
        } catch (ClassNotFoundException e) {
            Messages.error("Nie znaleziono klasy:" + e.getMessage());
        }
        if (exdendetClass != null) {
            Class<?> klasa = modul.getSuperclass();
                if (klasa == classProcessor) {
                    return true;
                }
        }
        return false;
    }
public ArrayList<T> GetAll(ArrayList<T> moduly, String interfejs){
    if(moduly==null || interfejs.isEmpty()) return null;
    ArrayList<T> rmoduly = new ArrayList<T>();
    for(T modul : moduly){
        if (isInterface(modul.getClass(), interfejs))
        {
            rmoduly.add(modul);
        }
    }
    return rmoduly;
}
public ArrayList<T> LoadByInterface(String packageName, String interfejs){
		File[] klasy = new File("./build/classes/"+packageName).listFiles(new ExtensionsFilter("class"));
		if(klasy==null) return null;
                ArrayList<T> moduly = new ArrayList<T>();
		for(File f: klasy)
		{
			Class<?> modul = null;
			try {
				modul = Class.forName(packageName+"."+f.getName().replace(".class", ""));
			} catch (Exception e) {
				Messages.error(e.getMessage());
			}
			if(isInterface(modul,interfejs)){
				try {
					moduly.add((T)modul.newInstance());
				} catch (InstantiationException e) {
					Messages.error(e.getMessage());
				} catch (IllegalAccessException e) {
					Messages.error(e.getMessage());
				}
			}
		}
                return moduly;

	}
public ArrayList<T> LoadByClass(String packageName, String className){
		File[] klasy = new File("./build/classes/"+packageName).listFiles(new ExtensionsFilter("class"));
		if(klasy==null) return null;
                ArrayList<T> moduly = new ArrayList<T>();
		for(File f: klasy)
		{
			Class<?> modul = null;
			try {
				modul = Class.forName(packageName+"."+f.getName().replace(".class", ""));
			} catch (ClassNotFoundException e) {
				Messages.error(e.getMessage());
			}
			if(isClass(modul,className)){
				try {
					moduly.add((T)modul.newInstance());
				} catch (InstantiationException e) {
					Messages.error(e.getMessage());
				} catch (IllegalAccessException e) {
					Messages.error(e.getMessage());
				}
			}
		}
                return moduly;

	}

}
