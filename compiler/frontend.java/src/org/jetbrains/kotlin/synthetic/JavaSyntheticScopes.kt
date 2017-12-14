/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.synthetic

import org.jetbrains.kotlin.incremental.components.LookupTracker
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.load.java.components.SamConversionResolver
import org.jetbrains.kotlin.resolve.DeprecationResolver
import org.jetbrains.kotlin.resolve.scopes.SyntheticScopes
import org.jetbrains.kotlin.storage.StorageManager
import org.jetbrains.kotlin.synthetic.extensions.SyntheticScopeProviderExtension

class JavaSyntheticScopes(
        storageManager: StorageManager,
        lookupTracker: LookupTracker,
        samConventionResolver: SamConversionResolver,
        deprecationResolver: DeprecationResolver,
        project: Project
): SyntheticScopes {
    override val scopeProviders =
            SyntheticScopeProviderExtension.getInstances(project).map { it.getProvider(storageManager) } +
            listOf(
                    JavaSyntheticPropertiesProvider(storageManager, lookupTracker),
                    SamAdapterSyntheticMembersProvider(storageManager, samConventionResolver, deprecationResolver),
                    SamAdapterSyntheticStaticFunctionsProvider(storageManager, samConventionResolver, lookupTracker),
                    SamAdapterSyntheticConstructorsProvider(storageManager, samConventionResolver, lookupTracker)
            )
}
