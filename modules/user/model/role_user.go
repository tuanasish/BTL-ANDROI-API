package model

import (
	"database/sql/driver"
	"encoding/json"
	"errors"
	"fmt"
	"strings"
)

type RoleUser int

const (
	RoleUserCustumer RoleUser = iota
	RoleUserAdmin
	RoleUserManager
)

//lay du lieu tu DB

func (role RoleUser) String() string {
	if int(role) < len(allRoleUser) {
		return allRoleUser[role]
	}
	return "Unknown"
}

var allRoleUser = [3]string{"admin", "customer", "manager"}

func parseStrRoleUser(s string) (RoleUser, error) {
	for i := range allRoleUser {
		if s == allRoleUser[i] {
			return RoleUser(i), nil
		}
	}
	return RoleUser(0), nil
}

func (role *RoleUser) Scan(value interface{}) error {
	if value == nil {
		*role = RoleUserCustumer // Giá trị mặc định nếu NULL
		return nil
	}
	switch v := value.(type) {
	case string:
		r, err := parseStrRoleUser(v)
		if err != nil {
			return err
		}
		*role = r
		return nil
	case []byte:
		r, err := parseStrRoleUser(string(v))
		if err != nil {
			return fmt.Errorf("error parsing status: %v", err)
		}
		*role = r
		return nil
	default:
		return errors.New("fail to scan from database: invalid type")
	}
}

func (role RoleUser) MarshalJSON() ([]byte, error) {
	return json.Marshal(role.String()) // Chỉ marshal thành chuỗi
}

//day du lieu vao db

func (role RoleUser) Value() (driver.Value, error) {
	if int(role) >= len(allRoleUser) {
		return nil, errors.New("invalid role user")
	}
	return allRoleUser[role], nil
}

func (role *RoleUser) UnmarshalJSON(data []byte) error {
	str := strings.Trim(string(data), "\"")
	v, err := parseStrRoleUser(str)
	if err != nil {
		return err
	}
	*role = v
	return nil
}
