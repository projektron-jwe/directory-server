/*
 *   Copyright 2004 The Apache Software Foundation
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package org.apache.ldap.server.prefs;


import java.util.prefs.Preferences;
import java.util.prefs.BackingStoreException;

import org.apache.ldap.server.jndi.AbstractJndiTest;


/**
 * Tests the ServerSystemPreferences class.
 *
 * @author <a href="mailto:directory-dev@incubator.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
public class ServerSystemPreferencesTest extends AbstractJndiTest
{
    /**
     * Tests to make sure the system preferences root has entry (test, abc123).
     */
    public void testRoot() throws BackingStoreException
    {
        ServerSystemPreferences prefs = new ServerSystemPreferences();

        assertEquals( "abc123", prefs.get( "test", "not the value" ) );

        String[] keys = prefs.keys();

        assertEquals( 1, keys.length );

        assertEquals( "test", keys[0] );
    }


    /**
     * Tests the creation and use of a new preferences node.
     *
     * @throws BackingStoreException if there are failures with the store
     */
    public void testCreate() throws BackingStoreException
    {
        Preferences prefs = new ServerSystemPreferences();

        Preferences testNode = prefs.node( "testNode" );

        testNode.put( "testNodeKey", "testNodeValue" );

        testNode.sync();
    }




    /**
     * Tests the creation and use of a new preferences node.
     *
     * @throws BackingStoreException if there are failures with the store
     */
    public void testCreateAndDestroy() throws BackingStoreException
    {
        Preferences prefs = new ServerSystemPreferences();

        Preferences testNode = prefs.node( "testNode" );

        testNode.put( "testNodeKey", "testNodeValue" );

        testNode.sync();

        testNode.putBoolean( "boolKey", true );

        testNode.putByteArray( "arrayKey", new byte[10] );

        testNode.putDouble( "doubleKey", 3.14 );

        testNode.putFloat( "floatKey", ( float ) 3.14 );

        testNode.putInt( "intKey", 345 );

        testNode.putLong( "longKey", 75449559185447L );

        testNode.sync();

        testNode = prefs.node( "testNode" );

        assertEquals( true, testNode.getBoolean( "boolKey", false ) );
    }
}
