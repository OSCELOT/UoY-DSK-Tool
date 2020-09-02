/**
 *  Copyright (C) 2016 University of York, UK.
 *
 *  This project was initiated through a donation of source code by the
 *  University of York, UK. It contains free software; you can redistribute
 *  it and/or modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation; either version 2 of the
 *  License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *  For more information please contact:
 *
 *  Web Services Group
 *  IT Service
 *  University of York
 *  YO10 5DD
 *  United Kingdom
 */
package uk.ac.york.its.vle.b2.factory;

import java.lang.reflect.Method;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;

import blackboard.platform.contentsystem.service.ContentSystemServiceFactory;
import blackboard.platform.context.ContextManagerFactory;
import blackboard.platform.plugin.PlugInManagerFactory;

/**
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */

@SuppressWarnings("rawtypes")
public class BlackboardFactory implements FactoryBean{

	private Class clazz;
	
	@Required
	public void setClassName(String className) throws ClassNotFoundException{
		clazz=Class.forName(className);
	}
	
	public Object getObject() throws Exception {
		
		// Return the ConetxtManager
		if(clazz.getName().equals("blackboard.platform.context.ContextManager")){
			return ContextManagerFactory.getInstance();
		}else if(clazz.getName().equals("blackboard.platform.plugin.PlugInManager")){
			return PlugInManagerFactory.getInstance();
		}else if(clazz.getName().equals("blackboard.platform.contentsystem.manager.DocumentManager")){
			return ContentSystemServiceFactory.getInstance().getDocumentManager();
		}else if(clazz.getName().equals("blackboard.platform.contentsystem.manager.SecurityManager")){
			return ContentSystemServiceFactory.getInstance().getSecurityManager();
		}
		
		// $ jar tf /usr/local/blackboard/systemlib/bb-platform.jar | grep -i default | grep -i loader"
		//
		// blackboard/persist/user/UserDbLoader$Default.class
		// blackboard/persist/user/UserRoleDbLoader$Default.class
		//
		// the name of the inner class for the *DbLoader is ended with the $Default string
		String className=clazz.getName()+"$Default"; 
		
		// Java Reflection 
		Method method = clazz.getClassLoader().loadClass(className).getMethod("getInstance", new Class[0]);
		Object object=method.invoke(clazz, new Object[0]);
		return object;
	}

	public Class getObjectType() {
		return clazz;
	}

	public boolean isSingleton() {
		return true;
	}

}
