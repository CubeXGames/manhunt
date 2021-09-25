package net.cubex.manhunt;

import java.util.HashMap;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class Main implements ModInitializer {
	
	public static Main instance;
	
	public static HashMap<String, String> trackedPlayers;
	
	@Override
	public void onInitialize() {
		
		instance = this;
		trackedPlayers = new HashMap<String, String>(64);
		
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			
			TrackCommand.register(dispatcher);
		});
	}
	
	/*static String getDescriptorForClass(final Class c)
	{
	    if(c.isPrimitive())
	    {
	        if(c==byte.class)
	            return "B";
	        if(c==char.class)
	            return "C";
	        if(c==double.class)
	            return "D";
	        if(c==float.class)
	            return "F";
	        if(c==int.class)
	            return "I";
	        if(c==long.class)
	            return "J";
	        if(c==short.class)
	            return "S";
	        if(c==boolean.class)
	            return "Z";
	        if(c==void.class)
	            return "V";
	        throw new RuntimeException("Unrecognized primitive "+c);
	    }
	    
	    if(c.isArray()) return c.getName().replace('.', '/');
	    return ('L'+c.getName()+';').replace('.', '/');
	}

	static String getMethodDescriptor(Method m)
	{
	    String s="(";
	    for(final Class c:(m.getParameterTypes()))
	        s+=getDescriptorForClass(c);
	    s+=')';
	    return (s+getDescriptorForClass(m.getReturnType())).replace('.', '/');
	}*/
}
