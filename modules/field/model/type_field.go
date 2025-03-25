package model

import (
	"database/sql/driver"
	"encoding/json"
	"errors"
)

type TypeField int

var (
	ErrTypeField = errors.New("TypeField không hợp lệ")
)

const (
	TypeFieldBadminton TypeField = iota
	TypeFieldPick
	TypeFieldFootball
	TypeFieldTenis
)

var allTypeField = [4]string{"Cầu lông", "Pick", "Bóng đá", "Tenis"}

// ✅ Sửa lỗi trong phương thức String()
func (f TypeField) String() string {
	if int(f) < len(allTypeField) {
		return allTypeField[f]
	}
	return "Unknown"
}

func ParseStrTypeField(s string) (TypeField, error) {
	for i, v := range allTypeField {
		if s == v {
			return TypeField(i), nil
		}
	}
	return TypeField(0), ErrTypeField
}

func (f TypeField) MarshalJSON() ([]byte, error) {
	return json.Marshal(f.String()) // Đảm bảo chuỗi JSON hợp lệ
}

// ✅ Sửa lỗi UnmarshalJSON()
func (f *TypeField) UnmarshalJSON(data []byte) error {
	var s string
	if err := json.Unmarshal(data, &s); err != nil {
		return err
	}
	parsedType, err := ParseStrTypeField(s)
	if err != nil {
		return err
	}
	*f = parsedType
	return nil
}

// ✅ Sửa lỗi Scan()
func (f *TypeField) Scan(value interface{}) error {
	if value == nil {
		*f = TypeFieldBadminton
		return nil
	}
	switch v := value.(type) {
	case string:
		temp, err := ParseStrTypeField(v)
		if err != nil {
			return err
		}
		*f = temp
		return nil
	case []byte:
		temp, err := ParseStrTypeField(string(v))
		if err != nil {
			return err
		}
		*f = temp
		return nil
	default:
		return ErrTypeField
	}
}

// ✅ Sửa lỗi Value()
func (f TypeField) Value() (driver.Value, error) {
	if int(f) < len(allTypeField) {
		return allTypeField[f], nil
	}
	return nil, ErrTypeField
}
