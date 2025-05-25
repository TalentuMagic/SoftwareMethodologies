import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/word.dart';

class ApiService {
  // Make sure this is your Spring Boot port:
  final String baseUrl = 'http://localhost:8080';

  Future<List<Word>> fetchWords() async {
    final resp = await http.get(Uri.parse('$baseUrl/words'));
    final data = json.decode(resp.body) as List;
    return data.map((e) => Word.fromJson(e as Map<String, dynamic>)).toList();
  }

  Future<Word> createWord(String name, String definition) async {
    final resp = await http.post(
      Uri.parse('$baseUrl/words'),
      headers: {'Content-Type': 'application/json'},
      body: json.encode({'name': name, 'definition': definition}),
    );
    return Word.fromJson(json.decode(resp.body));
  }

  Future<Word> updateWord(int id, String name, String definition) async {
    final resp = await http.put(
      Uri.parse('$baseUrl/words/$id'),
      headers: {'Content-Type': 'application/json'},
      body: json.encode({'name': name, 'definition': definition}),
    );
    return Word.fromJson(json.decode(resp.body));
  }

  Future<void> deleteWord(int id) async {
    await http.delete(Uri.parse('$baseUrl/words/$id'));
  }
}
