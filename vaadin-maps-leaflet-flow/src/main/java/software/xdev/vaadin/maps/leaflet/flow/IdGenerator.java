
package software.xdev.vaadin.maps.leaflet.flow;

/*-
 * #%L
 * vaadin-maps-leaflet-flow
 * %%
 * Copyright (C) 2019 XDEV Software
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public class IdGenerator
{
	private IdGenerator()
	{
		// private constructor for utility class
	}
	
	public static String generateId()
	{
		final String possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		final StringBuilder idStr = new StringBuilder();
		
		for(int i = 0; i < 10; i++)
		{
			idStr.append(possible.charAt((int)Math.floor(Math.random() * possible.length())));
		}
		return idStr.toString();
	}
	
}
