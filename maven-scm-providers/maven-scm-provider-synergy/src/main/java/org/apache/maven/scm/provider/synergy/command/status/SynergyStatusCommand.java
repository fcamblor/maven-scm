package org.apache.maven.scm.provider.synergy.command.status;

/*
 * Copyright 2001-2006 The Apache Software Foundation.
 *
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
 */

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.command.status.AbstractStatusCommand;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.synergy.command.SynergyCommand;
import org.apache.maven.scm.provider.synergy.repository.SynergyScmProviderRepository;
import org.apache.maven.scm.provider.synergy.util.SynergyUtil;

/**
 * @author <a href="mailto:julien.henry@capgemini.com">Julien Henry</a>
 */
public class SynergyStatusCommand extends AbstractStatusCommand implements SynergyCommand
{
    protected StatusScmResult executeStatusCommand( ScmProviderRepository repository, ScmFileSet fileSet )
            throws ScmException
    {
        getLogger().debug( "executing status command..." );

        SynergyScmProviderRepository repo = ( SynergyScmProviderRepository ) repository;
        getLogger().debug( "basedir: " + fileSet.getBasedir() );

        String CCM_ADDR = SynergyUtil.start( getLogger(), repo.getUser(), repo.getPassword(), null );

        List l;
        try
        {
            l = SynergyUtil.getWorkingFiles( getLogger(), repo.getProjectSpec(), repo.getProjectRelease(), CCM_ADDR );
        }
        finally
        {
            SynergyUtil.stop( getLogger(), CCM_ADDR );
        }

        List result = new LinkedList();
        for ( Iterator i = l.iterator(); i.hasNext(); )
        {

            ScmFile f = new ScmFile( ( String ) i.next(), ScmFileStatus.MODIFIED );
            result.add( f );
        }

        return new StatusScmResult( "ccm dir", result );
    }

}