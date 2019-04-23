/*
 * (C) Copyright 2016 OSIVIA (http://www.osivia.com)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.osivia.services.person.card.portlet.controller;

public class FormChgPwd {

    String currentPwd;
    String newPwd;
    String confirmPwd;

    /**
     * @return the currentPwd
     */
    public String getCurrentPwd() {
        return currentPwd;
    }
    /**
     * @param currentPwd the currentPwd to set
     */
    public void setCurrentPwd(String currentPwd) {
        this.currentPwd = currentPwd;
    }
    /**
     * @return the newPwd
     */
    public String getNewPwd() {
        return newPwd;
    }
    /**
     * @param newPwd the newPwd to set
     */
    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
    /**
     * @return the confirmPwd
     */
    public String getConfirmPwd() {
        return confirmPwd;
    }
    /**
     * @param confirmPwd the confirmPwd to set
     */
    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
    }




}