import 'package:flutter/material.dart';
import '../models/word.dart';
import '../services/api_service.dart';
import 'word_form_screen.dart';

class WordDetailScreen extends StatelessWidget {
  final Word word;
  final ApiService api;

  WordDetailScreen({Key? key, required this.word})
      : api = ApiService(),
        super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(word.name)),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(word.definition),
            const SizedBox(height: 20),
            Row(
              children: [
                ElevatedButton(
                  onPressed: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => WordFormScreen(word: word),
                      ),
                    );
                  },
                  child: const Text('Edit'),
                ),
                const SizedBox(width: 10),
                ElevatedButton(
                  onPressed: () {
                    api.deleteWord(word.id).then((_) {
                      Navigator.pop(context);
                    });
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.red,
                  ),
                  child: const Text('Delete'),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}