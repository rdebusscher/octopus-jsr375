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

import be.c4j.ee.security.role.GenericRoleVoter;
import be.rubus.octopus.jsr375.demo.permission.StarterRole;
import be.rubus.octopus.jsr375.demo.permission.StarterRoleCheck;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

/**
 *
 */
@Model
public class DemoView {

    @Inject
    @StarterRoleCheck(StarterRole.BAR)
    private GenericRoleVoter roleVoterBar;

    @Inject
    @StarterRoleCheck(StarterRole.KAZ)
    private GenericRoleVoter roleVoterBaz;

    public String hasRoleBar() {
        String result = "false";
        if (roleVoterBar.verifyPermission()) {
            result = "true";
        }
        return result;
    }

    public String hasRoleBaz() {
        String result = "false";
        if (roleVoterBaz.verifyPermission()) {
            result = "true";
        }
        return result;
    }
}
