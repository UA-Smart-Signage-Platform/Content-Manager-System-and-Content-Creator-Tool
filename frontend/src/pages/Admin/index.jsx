import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import adminService from '../../services/adminService';

const UserManagementContainer = styled.div`
  font-family: Arial, sans-serif;
  padding: 20px;
`;

const CreateUserContainer = styled.div`
  margin-bottom: 20px;
`;

const InputGroup = styled.div`
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
`;

const Input = styled.input`
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
`;

const Button = styled.button`
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background-color: #0056b3;
  }
`;

const UserList = styled.ul`
  list-style-type: none;
  padding: 0;
`;

const UserItem = styled.li`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #ccc;

  &:last-child {
    border-bottom: none;
  }
`;

const Select = styled.select`
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
`;

function UserManagement() {
  const [users, setUsers] = useState([]);
  const [newUser, setNewUser] = useState({
    email: '',
    role: '',
  });

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await adminService.getUsers();
      setUsers(response.data);
    } catch (error) {
      console.error('Error fetching users:', error);
    }
  };

  const createUser = async () => {
    try {
        await adminService.createUser(newUser);
      fetchUsers();
      setNewUser({
        email: '',
        role: '',
      });
    } catch (error) {
      console.error('Error creating user:', error);
    }
  };

  const deleteUser = async (id) => {
    try {
        await adminService.deleteUser(id);
      fetchUsers();
    } catch (error) {
      console.error('Error deleting user:', error);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setNewUser({
      ...newUser,
      [name.toLowerCase()]: value,
    });
  };

  return (
    <UserManagementContainer>
      <h2>User Management</h2>

      <CreateUserContainer>
        <h3>Create User</h3>
        <InputGroup>
          <Input
            type="text"
            name="email"
            value={newUser.email}
            placeholder="Email"
            onChange={handleChange}
          />
          <Select name="role" value={newUser.role} onChange={handleChange}>
            <option value="">Select Role</option>
            <option value="ADMIN">ADMIN</option>
            <option value="USER">USER</option>
          </Select>
          <Button onClick={createUser}>Create User</Button>
        </InputGroup>
      </CreateUserContainer>

      <div>
        <h3>User List</h3>
        <UserList>
          {users.map((user) => (
            <UserItem key={user.id}>
              {user.email}
                <span>{user.role}</span>
              <Button onClick={() => deleteUser(user.id)}>Delete</Button>
            </UserItem>
          ))}
        </UserList>
      </div>
    </UserManagementContainer>
  );
}

export default UserManagement;
