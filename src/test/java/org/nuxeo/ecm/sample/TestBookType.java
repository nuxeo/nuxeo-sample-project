package org.nuxeo.ecm.sample;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;
import org.nuxeo.ecm.core.repository.jcr.testing.RepositoryOSGITestCase;
import org.nuxeo.ecm.core.schema.DocumentType;

/*
 * (C) Copyright 2006-2007 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     gracinet
 *
 * $Id$
 */


/*
 * This Test Class tests whether we can create an instance of our new document type 'Book'
 * and whether the properties we set it with are the properties we get out of it when we do a read.
 * 
 * Version 5.1 extends RepositoryOSGITestCase which uses JCR implementation of repository
 */

public class TestBookType extends RepositoryOSGITestCase {
	
	private static final String OSGI_BUNDLE_NAME = "org.nuxeo.project.sample";
	private static final Log log = LogFactory.getLog(TestBookType.class);

	public void testBookCreation() throws Exception {
		
		// ----- SETUP SOME STUFF -----
		// To see logging on console set the log4j.properties file rootLogger to DEBUG
		log.debug("Starting ...");

		// path & name to our book
		String docPath = "/";
		String docName = "a_book";
		// book type as defined by the "doctype" in core-types-contrib.xml
		String bookType = "Book";
		// the book schema as defined by the schema name in core-types-contrib.xml
		String bookSchema = "book";
		
		// the book properties of the book we will create
		String bookIsbn = "ABC123";
		// even though rating is declared as an int in the book.xsd schema, we will set it as a long
		// because the getProperty() below returns it as a Long (the reason is that the underlying
		// system will store it as a long even when declared as an int). Then comparison in our test is easy.
		Long bookRating = new Long(3);
		Date bookDate = new Date();
		String [] bookKeywords = new String [] {"crime","thriller"};
		
		// Initialize the repository
		setUp();

        // Send our sample project bundle to the infrastructure
        deployBundle(OSGI_BUNDLE_NAME);

        // Open the repository 
        openRepository();

        // Get the session. This gives us access to the repository and hence Documents. 
        // The coreSession is provided for us from the superclass
        assertNotNull("Does our session exist?", coreSession);
        assertNotNull("Does our repository exist?", repository);
        
        
        // ----- GET OUR BOOK TYPE -----
        // Get a DocumentType that represents our new 'Book' type that we created via our config changes
        // by passing the name of our Book type as defined by "doctype" in core-types-contrib.xml
        DocumentType bookDocType = coreSession.getDocumentType(bookType);
        // make sure we successfully got a 'Book' type
        assertNotNull("Does our type exist?",bookDocType);
        
        // ----- CREATE OUR BOOK DOCUMENT MODEL -----
        // Now that we got the DocumentType successfully, lets actually
        // Create a model 'Book' object that represents a real Book: pass parameters 
        // Parent Path, path (these 2 specify the path to our document in the repository), name 
        // (the name of our Book type as defined by "doctype" in core-types-contrib.xml)
        DocumentModel modelDesired=new DocumentModelImpl(docPath, docName, bookType);
        // DocumentModel modelDesired = coreSession.createDocumentModel(docPath, docName, bookType);
        
        // Setup the properties that make it an Book: pass parameters, schema to use
        // property name, property value
        modelDesired.setProperty(bookSchema, "isbn", bookIsbn);
        modelDesired.setProperty(bookSchema, "rating", bookRating);
        modelDesired.setProperty(bookSchema, "publicationDate", bookDate);
        modelDesired.setProperty(bookSchema, "keywords", bookKeywords);

        // ----- CREATE OUR BOOK IN THE REPOSITORY -----
        // Create the Book in the repository based on the parameters we just setup
        // This is all done in memory so far
        DocumentModel modelResult = coreSession.createDocument(modelDesired);
        
        // See if we actually created the new Book in the repository ok
        assertNotNull(modelResult);

        // Now save our book to the session
        coreSession.saveDocument(modelResult);
        // Now save our session changes (ie. our book) to disk
        coreSession.save();
        
        // see if we can read the book from the repository on disk
        DocumentModel modelResultFromDisk = coreSession.getDocument(modelDesired.getRef());
        assertNotNull("book saved to repository on disk?", modelResultFromDisk);
        
        // Close the session and open a new one
        changeUser("system");

        // Read our doc back from the repository on disk
        modelResultFromDisk = null;
        modelResultFromDisk = coreSession.getDocument(modelDesired.getRef());
        assertNotNull("Does our new doc exist in repository on disk?", modelResultFromDisk);

        // ----- TEST THAT WHAT WE PUT IN IS WHAT WE GET OUT -----
        // Make sure that the path to the new Book we have just created in the repository
        // is the same as the parent path (/) plus our new path: after all thats what we specified above
        assertEquals("path is same?",docPath + docName, modelResultFromDisk.getPathAsString());
        assertEquals("path is same? (sanity)", docPath + docName, modelDesired.getPathAsString());
        
        // Now see if the properties of the book are the same as the ones we created
        // the date we get back from getProperty is a Calendar
        Calendar actualDate = (Calendar) modelResultFromDisk.getProperty(bookSchema, "publicationDate");
        assertEquals(bookIsbn, modelResultFromDisk.getProperty(bookSchema, "isbn"));
        assertEquals(bookRating, modelResultFromDisk.getProperty(bookSchema, "rating"));
        assertEquals(bookDate.getTime(), actualDate.getTime().getTime());
        // Note: In 5.1 String [] set with setProperty() comes back as String [] with getProperty()
        //       In 5.2 String [] set with setProperty() comes back as Object [] with getProperty()
        assertEquals(Arrays.asList(bookKeywords), Arrays.asList((String []) modelResultFromDisk.getProperty(bookSchema, "keywords")));
        // assertTrue(Arrays.equals((String [])bookKeywords, (String []) modelResult.getProperty(bookSchema, "keywords")));
        
        
        // now clean up: delete the Book document from disk so that any subsequent tests don't suffer from 
        // any side-effects of its existence
        coreSession.cancel();
        tearDown();
        
        // make sure it was deleted from disk
        

        

	}

}
