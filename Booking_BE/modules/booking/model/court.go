package model

type Court struct {
	ID      uint   `json:"court_id" gorm:"primaryKey"`
	FieldID uint   `json:"field_id" gorm:"field_id"`
	Number  int    `json:"court_number" gorm:"court_number"`
	Status  string `json:"status" gorm:"status"` // "available" or "booked"
}

func (b Court) TableName() string {
	return "court"
}
