/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportbook.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author minna
 */
public class UserTest {
    
    User user;
        
    @Before
    public void setUp() {
        user = new User(1, "hello", "world");
    }
    
    @Test
    public void usersWithSameUsernameEqual() {
        User other = new User(4, "hello", "sailor");
        assertTrue(user.equals(other));
    }
    
    @Test
    public void usersWithDifferentUsernamesDoNotEqual() {
        User other = new User(1, "wonderful", "world");
        assertFalse(user.equals(other));
    }
    
    @Test
    public void userAndObjectOfOtherTypeDoNotEqual() {
        Object o = new Object();
        assertFalse(user.equals(o));
    }
}
