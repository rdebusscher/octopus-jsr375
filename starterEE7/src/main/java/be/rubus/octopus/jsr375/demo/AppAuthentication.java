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
package be.rubus.octopus.jsr375.demo;

import be.c4j.ee.security.model.UserPrincipal;
import be.c4j.ee.security.realm.AuthenticationInfoBuilder;
import be.c4j.ee.security.realm.AuthorizationInfoBuilder;
import be.c4j.ee.security.realm.SecurityDataProvider;
import be.c4j.ee.security.role.NamedApplicationRole;
import be.c4j.ee.security.role.NamedRole;
import be.c4j.ee.security.role.RoleLookup;
import be.rubus.octopus.jsr375.demo.jsr375.IdentityStoreMatcher;
import be.rubus.octopus.jsr375.demo.permission.StarterRole;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.security.identitystore.annotation.Credentials;
import javax.security.identitystore.annotation.EmbeddedIdentityStoreDefinition;
import java.util.ArrayList;
import java.util.List;

@EmbeddedIdentityStoreDefinition({
        @Credentials(callerName = "reza", password = "secret1", groups = {"foo", "bar"}),
        @Credentials(callerName = "alex", password = "secret2", groups = {"foo", "kaz"}),
        @Credentials(callerName = "arjan", password = "secret3", groups = {"foo"})})
@ApplicationScoped
public class AppAuthentication implements SecurityDataProvider {

    private int principalId = 0;

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) {

        if (token instanceof UsernamePasswordToken) {
            UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

            AuthenticationInfoBuilder authenticationInfoBuilder = new AuthenticationInfoBuilder();
            authenticationInfoBuilder.principalId(principalId++).name(token.getPrincipal().toString());
            authenticationInfoBuilder.password(usernamePasswordToken.getPassword());

            authenticationInfoBuilder.externalPasswordCheck();

            return authenticationInfoBuilder.build();
        }
        return null;
    }


    @Override
    public AuthorizationInfo getAuthorizationInfo(PrincipalCollection principals) {

        AuthorizationInfoBuilder builder = new AuthorizationInfoBuilder();
        UserPrincipal principal = (UserPrincipal) principals.getPrimaryPrincipal();
        // We assume a 1-to-1 mapping between groups and roles.

        String[] groups = principal.getUserInfo(IdentityStoreMatcher.CALLER_GROUPS);
        for (String group : groups) {
            builder.addRole(new StarterNamedRole(group.toUpperCase()));
        }
        //builder.addRole()
        return builder.build();
    }

    @ApplicationScoped
    @Produces
    public RoleLookup<StarterRole> buildLookup() {
        // Make a typeSafe version of the roles.
        List<NamedApplicationRole> roles = new ArrayList<>();
        for (StarterRole starterRole : StarterRole.values()) {
            roles.add(new NamedApplicationRole(starterRole.name()));
        }
        return new RoleLookup<>(roles, StarterRole.class);
    }

    // Planned for Octopus 0.9.7 that we can use String instead of enums.

    private static class StarterNamedRole implements NamedRole {

        private String role;

        public StarterNamedRole(String role) {
            this.role = role;
        }

        @Override
        public String name() {
            return role;
        }
    }
}
