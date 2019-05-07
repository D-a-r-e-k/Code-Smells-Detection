/**
* The XMOJO Project 5
* Copyright � 2003 XMOJO.org. All rights reserved.

* NO WARRANTY

* BECAUSE THE LIBRARY IS LICENSED FREE OF CHARGE, THERE IS NO WARRANTY FOR
* THE LIBRARY, TO THE EXTENT PERMITTED BY APPLICABLE LAW. EXCEPT WHEN
* OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR OTHER PARTIES
* PROVIDE THE LIBRARY "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED
* OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK AS
* TO THE QUALITY AND PERFORMANCE OF THE LIBRARY IS WITH YOU. SHOULD THE
* LIBRARY PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING,
* REPAIR OR CORRECTION.

* IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING WILL
* ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MAY MODIFY AND/OR REDISTRIBUTE
* THE LIBRARY AS PERMITTED ABOVE, BE LIABLE TO YOU FOR DAMAGES, INCLUDING ANY
* GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE
* USE OR INABILITY TO USE THE LIBRARY (INCLUDING BUT NOT LIMITED TO LOSS OF
* DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU OR THIRD
* PARTIES OR A FAILURE OF THE LIBRARY TO OPERATE WITH ANY OTHER SOFTWARE),
* EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN ADVISED OF THE POSSIBILITY OF
* SUCH DAMAGES.
**/

package javax.management.loading;

import java.util.ArrayList;
import java.util.Vector;

/**
 * This class describes the information of a remote mbean that is downloaded
 * from remote using MLET service. This class will be useful for the
 * MLET service to instantiate and register the mbean with the server.
 */
class SimpleMLet
{
	private String code = null;
	private String object = null;
	private Vector archive = null;
	private String codebase = null;
	private String name = null;
	private String version = null;
	private ArrayList argTypes = null;
	private ArrayList argValues = null;

	SimpleMLet()
	{
		argTypes = new ArrayList();
		argValues = new ArrayList();
	}

	String getCode()
	{
		return code;
	}

	void setCode(String code)
	{
		this.code = code;
	}

	String getObject()
	{
		return object;
	}

	void setObject(String object)
	{
		this.object = object;
	}

	Vector getArchive()
	{
		return archive;
	}

	void setArchive(Vector archive)
	{
		this.archive = archive;
	}

	String getCodebase()
	{
		return codebase;
	}

	void setCodebase(String codebase)
	{
		this.codebase = codebase;
	}

	String getName()
	{
		return name;
	}

	void setName(String name)
	{
		this.name = name;
	}

	String getVersion()
	{
		return version;
	}

	void setVersion(String version)
	{
		this.version = version;
	}

	ArrayList getArgTypes()
	{
		return argTypes;
	}

	void setArgTypes(ArrayList argTypes)
	{
		this.argTypes = argTypes;
	}

	ArrayList getArgValues()
	{
		return argValues;
	}

	void setArgValues(ArrayList argValues)
	{
		this.argValues = argValues;
	}
}