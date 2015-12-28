/*
 * Copyright 2014-2015 Rudy De Busscher (www.c4j.be)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package be.rubus.octopus.jsr375.demo.jsr375;

import be.c4j.ee.security.authentication.ExternalPasswordAuthenticationInfo;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.util.Initializable;

import javax.security.identitystore.CredentialValidationResult;
import javax.security.identitystore.IdentityStore;
import javax.security.identitystore.credential.Password;
import javax.security.identitystore.credential.UsernamePasswordCredential;
import java.util.List;

/**
 *
 */
public class IdentityStoreMatcher implements CredentialsMatcher, Initializable {

    public static final String CALLER_GROUPS = "callerGroups";
    public static final String CALLER_ROLES = "callerRoles";

    private IdentityStore identityStore;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken credentials = (UsernamePasswordToken) token;
        Password password = new Password(credentials.getPassword());
        UsernamePasswordCredential credential = new UsernamePasswordCredential(credentials.getUsername(), password);
        CredentialValidationResult validationResult = identityStore.validate(credential);

        boolean result = validationResult.getStatus().equals(CredentialValidationResult.Status.VALID);
        if (result) {
            ExternalPasswordAuthenticationInfo authenticationInfo = (ExternalPasswordAuthenticationInfo) info;
            authenticationInfo.addUserInfo(CALLER_GROUPS, createUserInfo(validationResult.getCallerGroups()));
            authenticationInfo.addUserInfo(CALLER_ROLES, createUserInfo(validationResult.getCallerRoles()));
        }
        return result;
    }

    private String[] createUserInfo(List<String> stringList) {
        if (stringList == null) {
            return null;
        } else {
            return stringList.toArray(new String[stringList.size()]);
        }
    }

    @Override
    public void init() throws ShiroException {
        identityStore = BeanProvider.getContextualReference(IdentityStore.class);
    }
}
