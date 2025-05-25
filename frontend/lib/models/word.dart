class Word {
  final int id;
  final String name;
  final String definition;

  Word({
    required this.id,
    required this.name,
    required this.definition,
  });

  factory Word.fromJson(Map<String, dynamic> json) {
    return Word(
      id: json['id'] as int,
      name: json['name'] as String,             // ← use 'name' here
      definition: json['definition'] as String,
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'name': name,                           // ← and here
        'definition': definition,
      };
}
