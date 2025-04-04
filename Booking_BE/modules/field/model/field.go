package model

type Field struct {
	Id          int        `json:"field_id" gorm:"column:field_id"`
	Name        string     `json:"name" gorm:"column:name"`
	Location    string     `json:"location" gorm:"column:location"`
	Type        *TypeField `json:"type" gorm:"column:type"`
	Price       float64    `json:"price" gorm:"column:price"`
	Capacity    int        `json:"capacity" gorm:"column:capacity"`
	Description string     `json:"description" gorm:"column:description"`
	Image       string     `json:"image" gorm:"column:image"`
	Coordinates string     `json:"coordinates" gorm:"column:coordinates"`
}

func (f Field) TableName() string { return "Fields" }

type FieldCreate struct {
	Name        string     `json:"name" gorm:"column:name"`
	Location    string     `json:"location" gorm:"column:location"`
	Type        *TypeField `json:"type" gorm:"column:type"`
	Price       float64    `json:"price" gorm:"column:price"`
	Coordinates string     `json:"coordinates" gorm:"column:coordinates"`
}

func (f FieldCreate) TableName() string {
	return Field{}.TableName()
}

type FieldUpdate struct {
	Name        string     `json:"name" gorm:"column:name"`
	Location    string     `json:"location" gorm:"column:location"`
	Type        *TypeField `json:"type" gorm:"column:type"`
	Price       float64    `json:"price" gorm:"column:price"`
	Description string     `json:"description" gorm:"column:description"`
	Image       string     `json:"image" gorm:"column:image"`
	Coordinates string     `json:"coordinates" gorm:"column:coordinates"`
}

func (f FieldUpdate) TableName() string {
	return Field{}.TableName()
}
