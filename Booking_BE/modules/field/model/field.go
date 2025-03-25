package model

type Field struct {
	Id         int        `json:"field_id" gorm:"field_id"`
	Name       string     `json:"name" gorm:"name"`
	Location   string     `json:"location" gorm:"location"`
	Type       *TypeField `json:"type" gorm:"type"`
	Price      string     `json:"price" gorm:"price"`
	Capacity   int        `json:"capacity" gorm : "capacity"`
	Desciption string     `json:"description" gorm:"description"`
	Image      string     `json:"image" gorm:"image"`
}

func (f Field) TableName() string { return "Fields" }

type FieldCreate struct {
	Name     string     `json:"name" gorm:"name" binding:"required,min=3,max=100"`
	Location string     `json:"location" gorm:"location" binding:"required"`
	Type     *TypeField `json:"type" gorm:"type" binding:"required"`
	Price    string     `json:"price" gorm:"price" binding:"required,number"`
}

func (f FieldCreate) TableName() string {
	return Field{}.TableName()
}

type FieldUpdate struct {
	Name       string     `json:"name" gorm:"name"`
	Location   string     `json:"location" gorm:"location"`
	Type       *TypeField `json:"type" gorm:"type"`
	Price      string     `json:"price" gorm:"price"`
	Desciption string     `json:"description" gorm:"description"`
	Image      string     `json:"image" gorm:"image"`
}

func (f FieldUpdate) TableName() string {
	return Field{}.TableName()
}
