package model

import (
	"database/sql/driver"
	"fmt"
	"strconv"
	"strings"
)

// Point định nghĩa tọa độ với kinh độ và vĩ độ.
type Point struct {
	Longitude float64 `json:"longitude"`
	Latitude  float64 `json:"latitude"`
}

// Value để chuyển Point sang giá trị cho DB (tạo chuỗi dạng "POINT(longitude latitude)")
func (p Point) Value() (driver.Value, error) {
	return fmt.Sprintf("POINT(%f %f)", p.Longitude, p.Latitude), nil
}

// Scan để chuyển đổi giá trị lấy từ DB về Point.
// Lưu ý: MySQL thường trả về []byte dạng WKT (Well-Known Text)
func (p *Point) Scan(src interface{}) error {
	// Giả sử dữ liệu trả về dạng []byte, ví dụ "POINT(105.854444 21.028511)"
	b, ok := src.([]byte)
	if !ok {
		return fmt.Errorf("cannot scan type %T into Point", src)
	}
	s := string(b)
	s = strings.TrimPrefix(s, "POINT(")
	s = strings.TrimSuffix(s, ")")
	parts := strings.Split(s, " ")
	if len(parts) != 2 {
		return fmt.Errorf("invalid POINT format")
	}
	lng, err := strconv.ParseFloat(parts[0], 64)
	if err != nil {
		return err
	}
	lat, err := strconv.ParseFloat(parts[1], 64)
	if err != nil {
		return err
	}
	p.Longitude = lng
	p.Latitude = lat
	return nil
}
