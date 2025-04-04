package model

// BookingTimeSlotRequest là DTO dùng để nhận request tạo đồng thời Booking và TimeSlot
type BookingTimeSlotRequest struct {
	// Thuộc tính chung
	FieldID   uint   `json:"field_id" binding:"required"`
	Date      string `json:"date" binding:"required"`       // Định dạng "2006-01-02"
	StartTime string `json:"start_time" binding:"required"` // Định dạng HH:mm:ss
	EndTime   string `json:"end_time" binding:"required"`

	// Thuộc tính riêng của Booking
	UserID     uint    `json:"user_id" binding:"required"`
	CourtID    uint    `json:"court_id" binding:"required"`
	TotalPrice float64 `json:"total_price" binding:"required"`
}
