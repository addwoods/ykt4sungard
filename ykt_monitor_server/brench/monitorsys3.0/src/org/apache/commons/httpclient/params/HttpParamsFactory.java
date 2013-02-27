/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//httpclient/src/java/org/apache/commons/httpclient/params/HttpParamsFactory.java,v 1.5 2004/05/13 04:01:22 mbecke Exp $
 * $Revision: 155418 $
 * $Date: 2005-02-26 08:01:52 -0500 (Sat, 26 Feb 2005) $
 *
 * ====================================================================
 *
 *  Copyright 1999-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
 
package org.apache.commons.httpclient.params;

/**
 * A factory for getting the default set of parameters to use when creating an instance of 
 * <code>HttpParams</code>.
 * 
 * @see org.apache.commons.httpclient.params.DefaultHttpParams#setHttpParamsFactory(HttpParamsFactory)
 * 
 * @since 3.0
 */
public interface HttpParamsFactory {

    /**
     * Gets the default parameters.  This method may be called more than once and is not required
     * to always return the same value.
     * 
     * @return an instance of HttpParams
     */
    HttpParams getDefaultParams();

}